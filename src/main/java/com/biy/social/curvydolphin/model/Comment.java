package com.biy.social.curvydolphin.model;

import com.biy.social.curvydolphin.entity.CommentsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;
    private UUID videoId;
    private User userId;
    private String text;
    private LocalDateTime createdAt;

    public CommentsEntity toEntity() {
        CommentsEntity entity = new CommentsEntity();

        entity.setId(id);
        entity.setVideoId(videoId);
        entity.setText(text);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public static Comment fromEntity(CommentsEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getVideoId(),
                User.fromEntity(entity.getUser()),
                entity.getText(),
                entity.getCreatedAt()
        );
    }
}