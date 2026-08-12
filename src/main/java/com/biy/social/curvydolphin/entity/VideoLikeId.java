package com.biy.social.curvydolphin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoLikeId implements Serializable {

    private UUID videoId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof VideoLikeId)) return false;

        VideoLikeId that = (VideoLikeId) o;

        return Objects.equals(videoId, that.videoId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, userId);
    }
}