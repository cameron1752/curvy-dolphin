package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.LikesEntity;
import com.biy.social.curvydolphin.entity.VideoLikeId;
import com.biy.social.curvydolphin.exceptions.LikesException;
import com.biy.social.curvydolphin.model.Like;
import com.biy.social.curvydolphin.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LikesService {

    @Autowired
    LikesRepository likesRepository;

    public List<Like> getLike(UUID videoId, Long userId) {
        VideoLikeId id = new VideoLikeId(videoId, userId);
        Optional<LikesEntity> foundLike = likesRepository.findById(id);

        if (foundLike.isEmpty()){
            throw new LikesException(videoId, userId);
        }else {
            return Collections.singletonList(Like.fromEntity(foundLike.get()));
        }
    }

    public List<Like> getLikesByVideo(UUID videoId) {
        return likesRepository
                .findByVideoId(videoId)
                .stream()
                .map(Like::fromEntity)
                .toList();
    }

    public List<Like> getLikesByUser(Long userId) {
        return likesRepository
                .findByUserId(userId)
                .stream()
                .map(Like::fromEntity)
                .toList();
    }

    public Like createLike(Like like) {
        if (likesRepository.existsByVideoIdAndUserId(like.getVideoId(), like.getUserId())) {
            throw new LikesException("Video [" + like.getVideoId() + "] already liked by user [" + like.getUserId() + "]");
        }

        LikesEntity entity = like.toEntity();

        entity.setCreatedAt(LocalDateTime.now());

        LikesEntity savedEntity =
                likesRepository.save(entity);

        return Like.fromEntity(savedEntity);
    }

    public void deleteLike(UUID videoId, Long userId) {
        VideoLikeId id = new VideoLikeId(videoId, userId);

        if (!likesRepository.existsById(id)) {
            throw new LikesException(videoId, userId);
        }

        likesRepository.deleteById(id);
    }

    public long getLikeCount(UUID videoId) {
        return likesRepository.countByVideoId(videoId);
    }

    public boolean hasUserLikedVideo(UUID videoId,Long userId) {

        return likesRepository.existsByVideoIdAndUserId(videoId, userId);
    }
}