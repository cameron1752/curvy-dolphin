package com.biy.social.curvydolphin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feed {
    List<FeedObject> feedObjects = new ArrayList<>();

    public void add(FeedObject feedObject){
        feedObjects.add(feedObject);
    }
}
