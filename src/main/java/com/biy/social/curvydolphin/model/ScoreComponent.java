package com.biy.social.curvydolphin.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"freshnessScore", "engagementScore", "distanceScore",
                    "like_rate", "comment_rate", "share_rate", "skip_rate",
                    "interaction_rate","watch_rate","completion_rate"})
public class ScoreComponent {

    private double distanceScore;
    private double freshnessScore;
    private double engagementScore;



    private double like_rate;
    private double comment_rate;
    private double share_rate;
    private double skip_rate;



    private double interaction_rate;
    private double watch_rate;
    private double completion_rate;

    private double age_hours;
    private double skip;
    private double complete;
    private double play;
    private double impression;
    private double like;
    private double share;
    private double comment;

}
