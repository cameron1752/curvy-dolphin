package com.biy.social.curvydolphin.model;

import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.entity.VideoInteractionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInteraction {

    private Long id;
    private Long userId;
    private UUID videoId;
    private VideoInteractionEventType eventType;
    private Long watchDuration;
    private Long videoDuration;
    private LocalDateTime createdAt;
    private Double latitude;
    private Double longitude;

    public VideoInteractionEntity toEntity() {

        VideoInteractionEntity entity =
                new VideoInteractionEntity();

        entity.setId(null);
        entity.setUserId(userId);
        entity.setVideoId(videoId);
        entity.setEventType(eventType);
        entity.setWatchDuration(watchDuration);
        entity.setVideoDuration(videoDuration);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);

        return entity;
    }

    public static VideoInteraction fromEntity(
            VideoInteractionEntity entity) {

        return new VideoInteraction(
                entity.getId(),
                entity.getUserId(),
                entity.getVideoId(),
                entity.getEventType(),
                entity.getWatchDuration(),
                entity.getVideoDuration(),
                entity.getCreatedAt(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }

}