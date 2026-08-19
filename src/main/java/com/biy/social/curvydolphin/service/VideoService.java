package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.exceptions.UserException;
import com.biy.social.curvydolphin.exceptions.VideoException;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.model.Video;
import com.biy.social.curvydolphin.repository.UserRepository;
import com.biy.social.curvydolphin.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VideoService {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikesService likesService;

    @Autowired
    CommentsService commentsService;

    // get by video ID
    public List<Video> getVideoById(UUID id){
        Optional<VideoEntity> videoEntity = videoRepository.getById(id);

        if (videoEntity.isEmpty()){
            throw new VideoException(id);
        } else {
            Video video = Video.fromEntity(videoEntity.get());

            video.setLikes(likesService.getLikeCount(video.getId()));
            video.setComments(commentsService.getCommentCount(video.getId()));

            return Collections.singletonList(video);
        }
    }

    // get by user ID
    public List<Video> getVideos(long id){
        Optional<UserEntity> user = userRepository.getByUserId(id);

        if (user.isEmpty()){
            throw new UserException(id);
        }

        List<VideoEntity> videoEntities =  videoRepository.getAllByUser(user.get());
        List<Video> videos = new ArrayList<>();

        for (VideoEntity entity : videoEntities){
            Video video = Video.fromEntity(entity);

            video.setLikes(likesService.getLikeCount(video.getId()));
            video.setComments(commentsService.getCommentCount(video.getId()));

            videos.add(video);
        }

        return videos;
    }

    // in the future this will need to not only add the record to the table but provide a
    // pre-signed URL for the upload to S3
    public Video addVideo(Video video){
        // fetch user entity
        Optional<UserEntity> userEntity = userRepository.getByUserId(video.getUserId().getUser_id());
        // null / empty check
        if (userEntity.isEmpty()){
            throw new UserException(video.getUserId().getUser_id());
        }
        // video -> entity
        VideoEntity videoEntity = video.toEntity();
        // set user entity
        videoEntity.setUser(userEntity.get());
        // save
        VideoEntity savedEntity = videoRepository.save(videoEntity);
        // entity -> video
        return Video.fromEntity(savedEntity);
    }

    // in the future this will need to delete the record from the table
    // and delete the video from S3
    public void deleteVideo(UUID id) {
        // get video first ig
        Optional<VideoEntity> videoEntity = videoRepository.getById(id);

        // null / empty check
        if (videoEntity.isEmpty()){
            throw new VideoException(id);
        } else {
            videoRepository.deleteById(id);
        }
    }

    public Video editVideo(UUID id, Video video){
        Optional<VideoEntity> videoEntity = videoRepository.getById(id);

        if (videoEntity.isEmpty()){
            throw new VideoException(id);
        } else {
            VideoEntity entity = videoEntity.get();

            if (video.getThumbnailUrl() != null) {
                entity.setThumbnailUrl(video.getThumbnailUrl());
            }

            if (video.getCaption() != null){
                entity.setCaption(video.getCaption());
            }

            if (video.getVisibility() != null){
                entity.setVisibility(video.getVisibility());
            }

            VideoEntity saved = videoRepository.save(entity);
            return Video.fromEntity(saved);
        }
    }

}
