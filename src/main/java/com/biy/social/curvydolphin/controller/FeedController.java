package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.model.Video;
import com.biy.social.curvydolphin.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @GetMapping
    public ResponseEntity<List<Video>> getFeed(@RequestHeader(value = "traceId") String traceId,
                                               @RequestHeader(value = "user_id") long id){

        return ResponseEntity.ok(feedService.getFeed(id));
    }
}
