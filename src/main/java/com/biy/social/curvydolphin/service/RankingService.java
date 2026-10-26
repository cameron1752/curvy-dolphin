package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.biy.social.curvydolphin.constants.VideoInteractionEventType.*;

@Service
@Slf4j
public class RankingService {
    @Autowired
    VideoInteractionsService videoInteractionsService;
    private static final int age_halflife = 96;
    private static final int distance_halflife = 25;
    private static final int flat_zone_distance = 5;
    private static final double prior_strength = 20.0;

    // interaction score weights
    private static final double like_weight = .1;
    private static final double comment_weight = .2;
    private static final double share_weight = .4;
    private static final double skip_weight = .3;

    // engagement score weights
    private static final double watch_weight = .2;
    private static final double complete_weight = .4;
    private static final double interaction_weight = .4;

    // final score weights
    private static final double freshness_weight = .2;
    private static final double distance_weight = .5;
    private static final double engagement_weight = .3;

    // target values for interactions
    private static final double like_target = .10;
    private static final double comment_target = .02;
    private static final double share_target = .02;
    private static final double skip_target = .5;
    private static final double watch_target = .5;
    private static final double completion_target = .3;

    public Feed rankVideo(Feed feed, Location current){
        // one time check per feed
        OffsetDateTime now = OffsetDateTime.now();

        for (FeedObject feedObject : feed.getFeedObjects()){
            // location proximity
            scoreLocation(feedObject, current);
            // video interactions recently
            // interactions
            scoreInteractions(feedObject);
            // video age
            scoreAge(feedObject, now);
            // calculate the final score
            calculateScore(feedObject);
        }

        return feed;
    }

    private void calculateScore(FeedObject feedObject) {

        ScoreComponent scoreComponent = feedObject.getScoreComponent();

        double score = (scoreComponent.getFreshnessScore() * freshness_weight)
                     + (scoreComponent.getDistanceScore() * distance_weight)
                     + (scoreComponent.getEngagementScore() * engagement_weight);

        feedObject.setScore(score);

    }

    public void scoreInteractions(FeedObject feedObject){

        // get the interactions
        List<VideoInteraction> interactions = videoInteractionsService.getInteractionsByVideo(feedObject.getVideo().getId());

        feedObject.setVideoInteractionList(interactions);
        ScoreComponent scoreComponent = feedObject.getScoreComponent();

        double completion_rate  = 0.0;
        double watch_rate       = 0.0;
        double interaction_rate = 0.0;
        double like_rate        = 0.0;
        double comment_rate     = 0.0;
        double share_rate       = 0.0;
        double skip_rate        = 0.0;

        double impressions      = 0.0;
        double plays            = 0.0;
        double completes        = 0.0;
        double likes            = 0.0;
        double comments         = 0.0;
        double shares           = 0.0;
        double skips            = 0.0;

        // for each interaction
        for (VideoInteraction vi : interactions){
            log.info("video [{}] has this interaction [{}]", feedObject.getVideo().getId(), vi);
            switch (vi.getEventType()){
                case IMPRESSION -> impressions++;
                case PLAY -> plays++;
                case WATCH_COMPLETE -> completes++;
                case LIKE -> likes++;
                case COMMENT -> comments++;
                case SHARE -> shares++;
                default -> {}
            }
        }

        skips = countSkips(interactions);

        // like rate
        // how many users that were served and played liked the video
        // normalized to rate / (rate + target)
        like_rate = normalize(smoothed(likes, plays, like_target), like_target);
        scoreComponent.setLike_rate(like_rate);

        // comment rate
        // how many users that were served and played commented on the video
        // normalized to rate / (rate + target)
        comment_rate = normalize(smoothed(comments, plays, comment_target), comment_target);
        scoreComponent.setComment_rate(comment_rate);

        // share
        // how many users that were served and played shared the video
        // normalized to rate / (rate + target)
        share_rate = normalize(smoothed(shares, plays, share_target), share_target);
        scoreComponent.setShare_rate(share_rate);

        // skip rate
        // how many users that were served just skipped or watched < 25% of the video
        // normalized to rate / (rate + target)
        skip_rate = normalize(smoothed(skips, impressions, skip_target), skip_target);
        scoreComponent.setSkip_rate(skip_rate);

        // engagement rate
        interaction_rate =
                ((like_rate * like_weight) +
                (comment_rate * comment_weight) +
                (share_rate * share_weight)) -
                (skip_rate * skip_weight);

        // max interaction score possible for rescaling the score
        double interactionMax = like_weight * normalize(1, like_target) +
                comment_weight * normalize(1, comment_target) +
                share_weight * normalize(1, share_target);

        // min interaction score possible for rescaling the score
        double interactionMin = -skip_weight * normalize(1, skip_target);

        // calculate the interaction score
        double interactionScore = (interaction_rate - interactionMin) / (interactionMax - interactionMin);

        // set the value
        scoreComponent.setInteraction_rate(interaction_rate);

        // look at completion rate
        // complete vs plays
        completion_rate = smoothed(completes, plays, completion_target);
        scoreComponent.setCompletion_rate(completion_rate);

        // watch rate
        watch_rate = smoothed(plays, impressions, watch_target);
        scoreComponent.setWatch_rate(watch_rate);

        double score =
                ((watch_rate * watch_weight)
                + (completion_rate * complete_weight)
                + (interactionScore * interaction_weight)) * 10;

        scoreComponent.setEngagementScore(score);

        feedObject.setScoreComponent(scoreComponent);
    }


    // method that adjusts the current score based on the age of the video
    // math is like this:
    // log base e (2)
    // divided by halflife age (in hours)
    // final score is ( e ^ - LAMBDA * (age in hours) ) * 10
    // exponential decay for age
    public void scoreAge(FeedObject feedObject, OffsetDateTime now){
        double freshness = Duration.between(feedObject.getVideo().getCreatedAt(), now).toMinutes() / 60.0;

        double score = decay(freshness, age_halflife);

        feedObject.getScoreComponent().setAge_hours(freshness);
        feedObject.getScoreComponent().setFreshnessScore(score);

    }


    // method that adjusts the current score based on the location of the video vs location of user
    // math is like this:
    // log base e (2)
    // divided by half life distance threshold (km)
    // final score is ( e ^ - LAMBDA * (distance halflife (km)) ) * 10
    // exponential decay for distance after a flat zone distance
    public void scoreLocation(FeedObject feedObject, Location current){
        if (feedObject.getVideo().getId().toString().equals("b746bbbc-78a8-48a3-9fb7-92410f8d8196")){
            log.info ("just need to stop");
        }

        Video video = feedObject.getVideo();
        Location videoLoc = new Location(video.getLatitude(), video.getLongitude());
        double distance = getDistance(current, videoLoc);
        // current location vs video location
        log.info("distance between me and video location: {} km", distance);

        double score = decay(distance - flat_zone_distance, distance_halflife);

        feedObject.setDistance(distance);
        feedObject.getScoreComponent().setDistanceScore(score);
    }

    // helper method for shared decay method
    // returns the score for a component based on the halflife value passed in
    // Math.max(0, x) is for negative protection -> everything should be positive
    private static double decay(double x, double halflife){
        return 10 * Math.exp(-Math.log(2) / halflife * Math.max(0, x));
    }

    // function to normalize values between 0 and 1 given a target value and
    // the actual value used to normalize rates of interactions
    // keeps everything in the same scale
    // actualValue = .5 targetValue = .5 -> .5 / (1) -> .5
    private double normalize(double actualValue, double targetValue){
        return actualValue / (actualValue + targetValue);
    }

    // method to count skips based on previous interactions and on a user level
    // groups interactions by user
    // interactions for a user are sorted on time
    // iterated over and counted if a SKIP follows an early interaction indicator
    // so that IMPRESSION -> SKIP = 1 (is a skip)
    // so that IMPRESSION -> WATCH_75 -> SKIP = 0 (is not a skip)
    // all skip events are summed and returned for skips
    private double countSkips(List<VideoInteraction> interactions){
        return interactions.stream()
                .collect(Collectors.groupingBy(VideoInteraction::getUserId))
                .values().stream()
                .mapToLong(userEvents -> {
                    userEvents.sort(Comparator.comparing(VideoInteraction::getCreatedAt));
                    long count = 0;
                    for (int i = 1; i < userEvents.size(); i++) {
                        VideoInteractionEventType prev = userEvents.get(i - 1).getEventType();
                        if (userEvents.get(i).getEventType() == SKIP
                                && (prev == IMPRESSION || prev == PLAY || prev == WATCH_25)) {
                            count++;
                        }
                    }
                    return count;
                })
                .sum();
    }

    // smoothing function to do a few things:
    // 1 - prevents NaN or errors due to 0s
    // 2 - prevents 1 play with 1 like blowing up artificially by injecting artificial data
    // at our target rate
    private static double smoothed(double hits, double trials, double priorRate){
        return (hits + priorRate * prior_strength) / (trials + prior_strength);
    }

    // distance calculation in km
    public double getDistance(Location current, Location video){
        double earthRadiusKm = 6371.0;

        double lat1Rad = Math.toRadians(current.getLatitude());
        double lat2Rad = Math.toRadians(video.getLatitude());

        double deltaLat = Math.toRadians(video.getLatitude() - current.getLatitude());
        double deltaLon = Math.toRadians(video.getLongitude() - current.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad)
                * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2)
                * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

}
