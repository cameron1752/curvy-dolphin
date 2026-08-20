package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.exceptions.UserException;
import com.biy.social.curvydolphin.model.Feed;
import com.biy.social.curvydolphin.model.FeedObject;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.model.Video;
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

    // in the future this will be an algorithm to determine which videos
    // this given user should be served
    public Feed getFeed(){
        Feed feed = new Feed();

        User user = authorizationService.getCurrentAccount();

        // get list of videos they haven't posted
        List<VideoEntity> videoEntities = videoRepository.findVideosNotByUser(user.getUser_id());


        for (VideoEntity entity : videoEntities){
            Video video = Video.fromEntity(entity);

            video.setLikes(likesService.getLikeCount(video.getId()));
            video.setComments(commentsService.getCommentCount(video.getId()));

            feed.add(new FeedObject(video, likesService.hasUserLikedVideo(video.getId())));
        }

        // return
        return feed;
    }
}
