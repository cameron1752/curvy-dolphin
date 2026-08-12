package com.biy.social.curvydolphin.repository;

import com.biy.social.curvydolphin.entity.LikesEntity;
import com.biy.social.curvydolphin.entity.VideoLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LikesRepository
        extends JpaRepository<LikesEntity, VideoLikeId> {

    List<LikesEntity> findByVideoId(UUID videoId);

    List<LikesEntity> findByUserId(Long userId);

    long countByVideoId(UUID videoId);

    boolean existsByVideoIdAndUserId(
            UUID videoId,
            Long userId
    );
}