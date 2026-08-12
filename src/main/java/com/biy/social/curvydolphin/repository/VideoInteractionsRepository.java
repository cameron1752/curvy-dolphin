package com.biy.social.curvydolphin.repository;

import com.biy.social.curvydolphin.entity.VideoInteractionEntity;
import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VideoInteractionsRepository extends JpaRepository<VideoInteractionEntity, Long> {

    List<VideoInteractionEntity> findByVideoIdOrderByCreatedAtDesc(UUID videoId);

    List<VideoInteractionEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<VideoInteractionEntity> findByVideoIdAndEventTypeOrderByCreatedAtDesc(UUID videoId, VideoInteractionEventType eventType);

    List<VideoInteractionEntity>
    findByUserIdAndEventTypeOrderByCreatedAtDesc(Long userId, VideoInteractionEventType eventType);

    long countByVideoId(UUID videoId);

    long countByVideoIdAndEventType(UUID videoId, VideoInteractionEventType eventType);

    long countByUserIdAndEventType(Long userId, VideoInteractionEventType eventType);
}