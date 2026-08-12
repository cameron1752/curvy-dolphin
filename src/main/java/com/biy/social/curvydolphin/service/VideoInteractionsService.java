package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.VideoInteractionEntity;
import com.biy.social.curvydolphin.exceptions.VideoInteractionsException;
import com.biy.social.curvydolphin.model.VideoInteraction;
import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.repository.VideoInteractionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VideoInteractionsService {

    @Autowired
    VideoInteractionsRepository interactionRepository;


    public List<VideoInteraction> getInteraction(Long id) {
        Optional<VideoInteractionEntity> entity = interactionRepository.findById(id);

        if (entity.isEmpty()){
            throw new VideoInteractionsException(id);
        }

        return Collections.singletonList(VideoInteraction.fromEntity(entity.get()));
    }

    public List<VideoInteraction> getInteractionsByVideo(UUID videoId) {

        return interactionRepository
                .findByVideoIdOrderByCreatedAtDesc(videoId)
                .stream()
                .map(VideoInteraction::fromEntity)
                .toList();
    }

    public List<VideoInteraction> getInteractionsByUser(Long userId) {

        return interactionRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(VideoInteraction::fromEntity)
                .toList();
    }


    public List<VideoInteraction> getInteractionsByVideoAndEvent(UUID videoId,VideoInteractionEventType eventType) {

        return interactionRepository
                .findByVideoIdAndEventTypeOrderByCreatedAtDesc(
                        videoId,
                        eventType
                )
                .stream()
                .map(VideoInteraction::fromEntity)
                .toList();
    }

    public List<VideoInteraction> getInteractionsByUserAndEvent(Long userId,VideoInteractionEventType eventType) {

        return interactionRepository
                .findByUserIdAndEventTypeOrderByCreatedAtDesc(
                        userId,
                        eventType
                )
                .stream()
                .map(VideoInteraction::fromEntity)
                .toList();
    }

    public VideoInteraction createInteraction(VideoInteraction interaction) {
        VideoInteractionEntity entity = interaction.toEntity();

        VideoInteractionEntity savedEntity = interactionRepository.save(entity);

        return VideoInteraction.fromEntity(savedEntity);
    }

    public void deleteInteraction(Long id) {
        Optional<VideoInteractionEntity> entity = interactionRepository.findById(id);

        if (entity.isEmpty()){
            throw new VideoInteractionsException(id);
        }

        interactionRepository.delete(entity.get());
    }

    public long getInteractionCount(UUID videoId) {
        return interactionRepository.countByVideoId(videoId);
    }

    public long getInteractionCountByEvent(UUID videoId, VideoInteractionEventType eventType) {
        return interactionRepository
                .countByVideoIdAndEventType(videoId, eventType);
    }

    public long getUserInteractionCountByEvent(Long userId, VideoInteractionEventType eventType) {
        return interactionRepository
                .countByUserIdAndEventType(userId, eventType);
    }
}