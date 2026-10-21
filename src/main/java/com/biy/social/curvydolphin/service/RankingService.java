package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.biy.social.curvydolphin.constants.VideoInteractionEventType.*;

@Service
@Slf4j
public class RankingService {
    @Autowired
    VideoInteractionsService videoInteractionsService;

    int distance_threshold = Integer.MAX_VALUE;

    public Feed rankVideo(Feed feed, Location current){
        // location proximity
        feed = scoreLocation(feed, current);
        // video interactions recently
        // interactions
        feed = scoreInteractions(feed);
        // friends vs randoms
        // video age
        feed = scoreAge(feed);



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

            ScoreComponent scoreComponent = new ScoreComponent();

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

                if (vi.getEventType() == SKIP){
                    scoreComponent.setSkip(scoreComponent.getSkip() + 1);
                }
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

            // age decay
            double freshness = Math.exp((double) -feedObject.getAge() / 7);

            double score =
                    (watch_rate * .40)
                    + (completion_rate * .15)
                    + (engagement_rate * .35)
                    + (freshness * .10);

            feedObject.setScore(score);


            scoreComponent.setFreshness(freshness);
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

    public Feed scoreAge(Feed feed){

        for (FeedObject feedObject : feed.getFeedObjects()){
            double freshness = Math.exp(-feedObject.getAge() / 7.0);
            double score = feedObject.getScore() + (freshness * .10);

            feedObject.setScore(score);
        }

        return feed;
    }


    public Feed scoreLocation(Feed feed, Location current){
        // closer is better
        List<FeedObject> removeList = new ArrayList<>();

        for (FeedObject feedObject : feed.getFeedObjects()){
            Video video = feedObject.getVideo();
            Location videoLoc = new Location(video.getLatitude(), video.getLongitude());
            double distance = getDistance(current, videoLoc);

            // current location vs video location
            log.info("distance between me and video location: {} km", distance);

            // removes anything greater than threshold
            if (distance >= distance_threshold){
                removeList.add(feedObject);
            } else {
                feedObject.setDistance(distance);
            }
        }

        // removes anything outside of threshold
        feed.getFeedObjects().removeAll(removeList);

//        // compare distance of each with each other
//        feed.getFeedObjects().sort(Comparator.comparing(FeedObject::getDistance));
//
//        // determine score based on proximity
//        int score = feed.getFeedObjects().size();
//
//        // set score for each object
//        for (FeedObject feedObject  : feed.getFeedObjects()){
//            feedObject.setScore(score--);
//        }

        return feed;
    }

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
