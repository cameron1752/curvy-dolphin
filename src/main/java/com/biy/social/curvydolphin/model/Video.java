package com.biy.social.curvydolphin.model;

import com.biy.social.curvydolphin.constants.Visibility;
import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private UUID id;
    private User userId;
    private String videoUrl;
    private String thumbnailUrl;
    private Double latitude;
    private Double longitude;
    private Long locationId;
    private String caption;
    private OffsetDateTime createdAt;
    private Visibility visibility;
    private long likes;
    private long comments;

    public Video(UUID id){
        this.id = id;
    }

    public static Video fromEntity(VideoEntity videoEntity) {
        return new Video(videoEntity.getId(),
                User.fromEntity(videoEntity.getUser()),
                videoEntity.getVideoUrl(),
                videoEntity.getThumbnailUrl(),
                videoEntity.getLatitude(),
                videoEntity.getLongitude(),
                videoEntity.getLocationId(),
                videoEntity.getCaption(),
                videoEntity.getCreatedAt(),
                videoEntity.getVisibility(),
                0,
                0);
    }

    public VideoEntity toEntity() {
        VideoEntity entity = new VideoEntity();
        entity.setId(UUID.randomUUID());

        entity.setVideoUrl("https://cdn.example.com/video/" + entity.getId() + ".mp4");
        entity.setThumbnailUrl("https://cdn.example.com/thumbs/" + entity.getId() + ".jpg");
        entity.setLatitude(this.latitude);
        entity.setLongitude(this.longitude);
        entity.setLocationId(this.locationId);
        entity.setCaption(this.caption);
        entity.setVisibility(this.visibility);
        entity.setCreatedAt(OffsetDateTime.now());

        return entity;
    }
}