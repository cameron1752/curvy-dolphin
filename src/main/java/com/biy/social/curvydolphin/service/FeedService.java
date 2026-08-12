package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.exceptions.UserException;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.model.Video;
import com.biy.social.curvydolphin.repository.UserRepository;
import com.biy.social.curvydolphin.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedService {


    @Autowired
    VideoRepository videoRepository;

    @Autowired
    UserRepository userRepository;

    // in the future this will be an algorithm to determine which videos
    // this given user should be served
    public List<Video> getFeed(long user_id){
        // confirm it's actual user
        Optional<UserEntity> userEntity = userRepository.getByUserId(user_id);

        if (userEntity.isEmpty()){
            throw new UserException(user_id);
        }

        // get list of videos they haven't posted
        List<VideoEntity> videoEntities = videoRepository.findVideosNotByUser(user_id);
        // return
        return videoEntities
                .stream()
                .map(Video::fromEntity)
                .toList();
    }
}
