package com.biy.social.curvydolphin.model;

import com.biy.social.curvydolphin.entity.LikesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private UUID videoId;
    private Long userId;
    private LocalDateTime createdAt;

    public LikesEntity toEntity() {
        LikesEntity entity = new LikesEntity();

        entity.setVideoId(videoId);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public static Like fromEntity(LikesEntity entity) {
        return new Like(
                entity.getVideoId(),
                entity.getUserId(),
                entity.getCreatedAt()
        );
    }
}