package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.exceptions.UserException;
import com.biy.social.curvydolphin.model.*;
import com.biy.social.curvydolphin.repository.UserRepository;
import com.biy.social.curvydolphin.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedService {


    @Autowired
    VideoRepository videoRepository;

    @Autowired
    LikesService likesService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    RankingService rankingService;

    // in the future this will be an algorithm to determine which videos
    // this given user should be served
    public Feed getFeed(Location current){
        // create feed object
        Feed feed = new Feed();
        // get current account
        User user = authorizationService.getCurrentAccount();
        // get list of videos they haven't posted
        List<VideoEntity> videoEntities = videoRepository.findVideosNotByUser(user.getUser_id());

        // for each of the found videos
        for (VideoEntity entity : videoEntities){
            // entity -> pojo
            Video video = Video.fromEntity(entity);
            // set likes & comments
            video.setLikes(likesService.getLikeCount(video.getId()));
            video.setComments(commentsService.getCommentCount(video.getId()));
            // get liked status from current user
            boolean hasLiked = likesService.hasUserLikedVideo(video.getId());
            // add feed object to feed with like status
            feed.add(new FeedObject(video, hasLiked));
        }

        // rank videos and return
       return rankingService.rankVideo(feed, current).sort();
    }
}
