package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.biy.social.curvydolphin.constants.VideoInteractionEventType.*;

@Service
@Slf4j
public class RankingService {
    @Autowired
    VideoInteractionsService videoInteractionsService;
    private static final int HALFLIFE_AGE = 24;
//    int distance_threshold = Integer.MAX_VALUE;
    int distance_threshold = 25;
    public Feed rankVideo(Feed feed, Location current){
        // location proximity
        feed = scoreLocation(feed, current);
        // video interactions recently
        feed = scoreAge(feed);
        // interactions
        feed = scoreInteractions(feed);
        // friends vs randoms
        // video age

       return calculateScore(feed);
    }

    private Feed calculateScore(Feed feed) {

        for (FeedObject feedObject : feed.getFeedObjects()){
            double score;

            ScoreComponent scoreComponent = feedObject.getScoreComponent();

            score = (scoreComponent.getFreshnessScore() * .10)
                + (scoreComponent.getDistanceScore() * .70)
                + (scoreComponent.getEngagementScore() * .20);

            feedObject.setScore(score);
        }

        return feed;
    }

    public Feed scoreInteractions(Feed feed){
        // for each object
        for (FeedObject feedObject : feed.getFeedObjects()){
            // get the interactions
            List<VideoInteraction> interactions = videoInteractionsService.getInteractionsByVideo(feedObject.getVideo().getId());

            feedObject.setVideoInteractionList(interactions);

            double completion_rate = 0.0;
            double watch_rate = 0.0;
            double engagement_rate = 0.0;
            double like_rate = 0;
            double comment_rate = 0;
            double share_rate = 0;
            double skip_rate = 0;

            ScoreComponent scoreComponent = feedObject.getScoreComponent();
            VideoInteraction previousVi = interactions.getFirst();
            // for each interaction
            for (VideoInteraction vi : interactions){
                log.info("video [{}] has this interaction [{}]", feedObject.getVideo().getId(), vi);
                // get the score
                feedObject.setScore(feedObject.getScore() + classifyInteraction(vi.getEventType()));
                if (vi.getEventType() == WATCH_COMPLETE){
                    scoreComponent.setComplete(scoreComponent.getComplete() + 1);
                }

                if (vi.getEventType() == PLAY){
                    scoreComponent.setPlay(scoreComponent.getPlay() + 1);
                }

                if (vi.getEventType() == IMPRESSION){
                    scoreComponent.setImpression(scoreComponent.getImpression() + 1);
                }

                if (vi.getEventType() == LIKE){
                    scoreComponent.setLike(scoreComponent.getLike() + 1);
                }

                if (vi.getEventType() == COMMENT){
                    scoreComponent.setComment(scoreComponent.getComment() + 1);
                }

                if (vi.getEventType() == SHARE){
                    scoreComponent.setShare(scoreComponent.getShare() + 1);
                }

                if (previousVi.getEventType() == WATCH_25 && vi.getEventType() == SKIP){
                    scoreComponent.setSkip(scoreComponent.getSkip() + 1);
                }else if (previousVi.getEventType() == WATCH_50 && vi.getEventType() == SKIP){
                    scoreComponent.setSkip(scoreComponent.getSkip() + .5);
                }else if (previousVi.getEventType() == WATCH_75 && vi.getEventType() == SKIP){
                    scoreComponent.setSkip(scoreComponent.getSkip() + .25);
                } else if (vi.getEventType() == SKIP){
                    scoreComponent.setSkip(scoreComponent.getSkip() + 1);
                }

                previousVi = vi;
            }

            feedObject.setScore(1.0);



            // look at completion rate
            // complete vs plays
            completion_rate = scoreComponent.getComplete() / scoreComponent.getPlay();

            // watch rate
            watch_rate = scoreComponent.getPlay() / scoreComponent.getImpression();

            // like rate
            like_rate = scoreComponent.getLike() / scoreComponent.getPlay();

            // comment rate
            comment_rate = scoreComponent.getComment() / scoreComponent.getPlay();

            // share
            share_rate = scoreComponent.getShare() / scoreComponent.getPlay();

            // skip rate
            skip_rate = scoreComponent.getSkip() / scoreComponent.getPlay();

            // engagement rate
            engagement_rate =
                    ((like_rate * 6) +
                    (comment_rate * 8) +
                    (share_rate * 10)) -
                    (skip_rate * 5);

//
            double score =
                    (watch_rate * .40)
                    + (completion_rate * .15)
                    + (engagement_rate * .35);

            if (feedObject.getVideo().getId().toString().equals("b746bbbc-78a8-48a3-9fb7-92410f8d8196")){
                log.info ("just need to stop");
            }

            if (Double.isNaN(score)){
                scoreComponent.setEngagementScore(0.0);
            } else {
                scoreComponent.setEngagementScore(score);
            }


////            feedObject.setScore(score);

            scoreComponent.setEngagement_rate(engagement_rate);
            scoreComponent.setCompletion_rate(completion_rate);
            scoreComponent.setWatch_rate(watch_rate);

            feedObject.setScoreComponent(scoreComponent);

        }

        return feed;
    }

    public double classifyInteraction(VideoInteractionEventType type){
        switch (type){
            case IMPRESSION:
                return 0.0;
            case PLAY:
                return .25;
            case WATCH_25:
                return .5;
            case WATCH_50:
                return 1.0;
            case WATCH_75:
                return 1.25;
            case WATCH_COMPLETE:
                return 3.0;
            case LIKE:
                return 2.0;
            case COMMENT:
                return 3.0;
            case SHARE:
                return 4.0;
            case SKIP:
                return -5.0;
            default:
                return 0.0;
        }
    }

    // method that adjusts the current score based on the age of the video
    // math is like this:
    // log base e (2) (higher number provides steeper curve)
    // divided by halflife age (in hours)
    // final score is ( e ^ - LAMBDA * (age in hours) ) * 10
    // exponential decay for age
    public Feed scoreAge(Feed feed){
        double LAMBDA = Math.log(2) / HALFLIFE_AGE;

        for (FeedObject feedObject : feed.getFeedObjects()){
            double freshness = Duration.between(feedObject.getVideo().getCreatedAt(), OffsetDateTime.now()).toMinutes() / 60.0;;
            double score = Math.exp(-LAMBDA * freshness) * 10;

            feedObject.getScoreComponent().setAge_hours(freshness);

            if (Double.isNaN(score)){
                feedObject.getScoreComponent().setFreshnessScore(0.0);
            } else {
                feedObject.getScoreComponent().setFreshnessScore(score);
            }

        }

        return feed;
    }


    // method that adjusts the current score based on the age of the video
    // math is like this:
    // log base e (16) (higher number provides steeper curve)
    // divided by halflife distance threshold (km)
    // final score is ( e ^ - LAMBDA * (distance threshold (km)) ) * 10
    // exponential decay for distance
    public Feed scoreLocation(Feed feed, Location current){
        // closer is better
        double LAMBDA = Math.log(16) / distance_threshold;

        for (FeedObject feedObject : feed.getFeedObjects()){
            if (feedObject.getVideo().getId().toString().equals("b746bbbc-78a8-48a3-9fb7-92410f8d8196")){
                log.info ("just need to stop");
            }

            Video video = feedObject.getVideo();
            Location videoLoc = new Location(video.getLatitude(), video.getLongitude());
            double distance = getDistance(current, videoLoc);
            // current location vs video location
            log.info("distance between me and video location: {} km", distance);

            double score;
            if (distance > distance_threshold){
                score = 0.0;
            } else {
                score = Math.exp(-LAMBDA * distance) * 10;
            }

            feedObject.setDistance(distance);
            feedObject.getScoreComponent().setDistanceScore(score);

        }

        return feed;
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
