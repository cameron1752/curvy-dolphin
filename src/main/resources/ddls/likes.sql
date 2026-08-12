CREATE TABLE IF NOT EXISTS curvy_dolphin.video_likes
(
    video_id   UUID NOT NULL,
    user_id    BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT video_likes_pkey
    PRIMARY KEY (video_id, user_id),

    CONSTRAINT fk_video_likes_video
    FOREIGN KEY (video_id)
    REFERENCES curvy_dolphin.videos (id)
    ON DELETE CASCADE,

    CONSTRAINT fk_video_likes_user
    FOREIGN KEY (user_id)
    REFERENCES curvy_dolphin.users (user_id)
    ON DELETE CASCADE
    );

CREATE INDEX idx_video_likes_user_id
    ON curvy_dolphin.video_likes (user_id);

CREATE INDEX idx_video_likes_video_id
    ON curvy_dolphin.video_likes (video_id);