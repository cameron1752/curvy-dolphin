package com.biy.social.curvydolphin.entity;

import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "video_interactions", schema = "curvy_dolphin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoInteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "video_id", nullable = false)
    private UUID videoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private VideoInteractionEventType eventType;

    @Column(name = "watch_duration")
    private Long watchDuration;

    @Column(name = "video_duration")
    private Long videoDuration;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}