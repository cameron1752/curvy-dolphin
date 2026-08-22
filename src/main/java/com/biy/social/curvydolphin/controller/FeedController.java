package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.model.Feed;
import com.biy.social.curvydolphin.model.Location;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.model.Video;
import com.biy.social.curvydolphin.service.FeedService;
import com.biy.social.curvydolphin.service.SeedClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    SeedClass seedClass;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public ResponseEntity<Feed> getFeed(@RequestHeader(value = "traceId") String traceId,
                                        @RequestHeader(value = "lat") Double latitude,
                                        @RequestHeader(value = "long") Double longitude){

//        seedClass.seed();

        return ResponseEntity.ok(feedService.getFeed(new Location(latitude, longitude)));
    }
}
