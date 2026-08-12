package com.biy.social.curvydolphin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "video_likes", schema = "curvy_dolphin")
@IdClass(VideoLikeId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesEntity {

    @Id
    @Column(name = "video_id", nullable = false)
    private UUID videoId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}