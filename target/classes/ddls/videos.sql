-- Table: curvy_dolphin.videos

-- DROP TABLE IF EXISTS curvy_dolphin.videos;

CREATE TABLE curvy_dolphin.videos (
                                      id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                      user_id        BIGINT NOT NULL,
                                      video_url      TEXT NOT NULL,
                                      thumbnail_url  TEXT,
                                      latitude       DOUBLE PRECISION,
                                      longitude      DOUBLE PRECISION,
                                      location_id    BIGINT,
                                      caption        TEXT,
                                      created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                      visibility     VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',

                                      CONSTRAINT fk_videos_user
                                          FOREIGN KEY (user_id) REFERENCES curvy_dolphin.users (user_id),

                                      CONSTRAINT chk_videos_visibility
                                          CHECK (visibility IN ('PUBLIC', 'PRIVATE', 'FRIENDS')),

                                      CONSTRAINT chk_videos_latitude
                                          CHECK (latitude BETWEEN -90 AND 90),

                                      CONSTRAINT chk_videos_longitude
                                          CHECK (longitude BETWEEN -180 AND 180)
);

CREATE INDEX idx_videos_user_id ON curvy_dolphin.videos (user_id);
CREATE INDEX idx_videos_created_at ON curvy_dolphin.videos (created_at DESC);

ALTER TABLE IF EXISTS curvy_dolphin.videos
    OWNER to postgres;