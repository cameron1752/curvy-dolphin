package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.exceptions.VideoException;
import com.biy.social.curvydolphin.model.Video;
import com.biy.social.curvydolphin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/videos")
public class VideoController {
    @Autowired
    VideoService videoService;

    @GetMapping
    public ResponseEntity<List<Video>> getVideo(@RequestHeader(value = "traceId") String traceId,
                                                @RequestHeader(value = "user_id", required = false) Long user_id,
                                                @RequestHeader(value = "video_id", required = false) UUID video_id){
        if (video_id != null){
            return ResponseEntity.ok(videoService.getVideoById(video_id));
        }
        if ( user_id != null && user_id != 0){
            return ResponseEntity.ok(videoService.getVideos(user_id));
        }

        throw new VideoException("user_id or video_id are required");
    }

    @PostMapping
    public ResponseEntity<Video> saveVideo(@RequestHeader(value = "traceId") String traceId,
                                           @RequestBody Video video){
        return ResponseEntity.ok(videoService.addVideo(video));
    }

    @PatchMapping
    public ResponseEntity<Video> editVideo(@RequestHeader(value = "traceId") String traceId,
                                           @RequestHeader(value = "video_id") UUID video_id,
                                           @RequestBody Video video){

        return ResponseEntity.ok(videoService.editVideo(video_id, video));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteVideo(@RequestHeader(value = "traceId") String traceId,
                                                           @RequestHeader(value = "video_id") UUID video_id){
        videoService.deleteVideo(video_id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Video deleted with Id: " + video_id);
        return ResponseEntity.ok(response);
    }
}
