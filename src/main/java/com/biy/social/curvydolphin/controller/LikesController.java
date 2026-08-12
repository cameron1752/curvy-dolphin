package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.exceptions.LikesException;
import com.biy.social.curvydolphin.model.Like;
import com.biy.social.curvydolphin.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/likes")
public class LikesController {

    @Autowired
    LikesService likesService;

    @GetMapping
    public ResponseEntity<List<Like>> getLikes(
            @RequestHeader(value = "traceId") String traceId,
            @RequestHeader(value = "video_id", required = false) UUID video_id,
            @RequestHeader(value = "user_id", required = false) Long user_id) {

        // get like on specific video for specific user
        if (video_id != null && user_id != null) {
            return ResponseEntity.ok(likesService.getLike(video_id,user_id));
        }
        // get likes on video
        if (video_id != null) {
            return ResponseEntity.ok(likesService.getLikesByVideo(video_id));
        }
        // get users likes
        if (user_id != null) {
            return ResponseEntity.ok(likesService.getLikesByUser(user_id));
        }
        throw new LikesException("video_id or user_id required!");
    }

    // Get like count for video
    @GetMapping("/count")
    public ResponseEntity<Map<String, String>> getLikeCount(
            @RequestHeader(value = "traceId") String traceId,
            @RequestHeader(value = "video_id") UUID video_id) {

        Map<String, String> response = new HashMap<>();
        response.put("video_id", video_id.toString());
        response.put("like_count", String.valueOf(likesService.getLikeCount(video_id)));
        return ResponseEntity.ok(response);
    }

    // Check whether user liked video
    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> hasUserLikedVideo(
            @RequestHeader(value = "traceId") String traceId,
            @RequestHeader(value = "video_id") UUID video_id,
            @RequestHeader(value = "user_id") Long user_id) {

        Map<String, String> response = new HashMap<>();
        response.put("video_id", video_id.toString());
        response.put("user_id", user_id.toString());
        response.put("value", String.valueOf(likesService.hasUserLikedVideo(video_id, user_id)));
        return ResponseEntity.ok(response);
    }

    // Like a video
    @PostMapping
    public ResponseEntity<Like> createLike(
            @RequestHeader(value = "traceId") String traceId,
            @RequestBody Like like) {

        Like createdLike = likesService.createLike(like);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdLike);
    }

    // Unlike a video
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteLike(
            @RequestHeader(value = "traceId") String traceId,
            @RequestHeader(value = "video_id") UUID video_id,
            @RequestHeader(value = "user_id") Long user_id) {

        likesService.deleteLike(video_id, user_id);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "video [" + video_id + "] unliked by [" + user_id + "]");
        return ResponseEntity.ok(response);
    }
}