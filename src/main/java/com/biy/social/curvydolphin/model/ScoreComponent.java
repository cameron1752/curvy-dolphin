package com.biy.social.curvydolphin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreComponent {

    private double watch_rate;
    private double age_hours;
    private double completion_rate;
    private double engagement_rate;
    private double freshnessScore;
    private double engagementScore;
    private double skip;
    private double complete;
    private double play;
    private double impression;
    private double like;
    private double share;
    private double comment;
    private double distanceScore;

}
