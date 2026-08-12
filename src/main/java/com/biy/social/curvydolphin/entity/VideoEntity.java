package com.biy.social.curvydolphin.entity;

import com.biy.social.curvydolphin.constants.Visibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(schema = "curvy_dolphin", name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "video_url", nullable = false, columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "caption", columnDefinition = "TEXT")
    private String caption;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 20)
    private Visibility visibility;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (visibility == null) {
            visibility = Visibility.PUBLIC;
        }
    }
}