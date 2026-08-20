package com.biy.social.curvydolphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedObject {
    private Video video;
    private boolean isLiked;

}
