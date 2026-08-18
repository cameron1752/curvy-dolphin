package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.exceptions.VideoException;
import com.biy.social.curvydolphin.exceptions.VideoInteractionsException;
import com.biy.social.curvydolphin.model.VideoInteraction;
import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.service.VideoInteractionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/v1/interactions")
public class VideoInteractionController {

    @Autowired
    VideoInteractionsService interactionsService;
    

    @GetMapping
    public ResponseEntity<List<VideoInteraction>> getInteraction(
            @RequestHeader(value = "traceId", required = true) String traceId,
            @RequestHeader(value = "interaction_id", required = false) Long interactionId,
            @RequestHeader(value = "video_id", required = false) UUID videoId,
            @RequestHeader(value = "user_id", required = false) Long userId,
            @RequestHeader(value = "event_type", required = false) VideoInteractionEventType eventType
    ) {

        // Specific interaction
        if (interactionId != null) {
            return ResponseEntity.ok(interactionsService.getInteraction(interactionId));
        }

        // Video + event
        if (videoId != null && eventType != null) {
            return ResponseEntity.ok(
                    interactionsService.getInteractionsByVideoAndEvent(videoId,eventType)
            );
        }

        // User + event
        if (userId != null && eventType != null) {
            return ResponseEntity.ok(
                    interactionsService.getInteractionsByUserAndEvent(userId,eventType));
        }

        // Video
        if (videoId != null) {
            return ResponseEntity.ok(interactionsService.getInteractionsByVideo(videoId));
        }

        // User
        if (userId != null) {
            return ResponseEntity.ok(interactionsService.getInteractionsByUser(userId));
        }

        throw new VideoInteractionsException("interaction_id, event_type + video_id, event_type + user_id, video_id, or user_id are required");
    }

    @PostMapping
    public ResponseEntity<VideoInteraction> createInteraction(
            @RequestHeader(value = "traceId", required = true) String traceId,
            @RequestBody VideoInteraction interaction) {

        VideoInteraction createdInteraction = interactionsService.createInteraction(interaction);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdInteraction);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteInteraction(
            @RequestHeader(value = "traceId", required = true) String traceId,
            @RequestHeader(value = "interaction_id", required = true) Long interactionId) {

        interactionsService.deleteInteraction(interactionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "interaction deleted with id [" + interactionId + "]");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, String>> getInteractionCount(
            @RequestHeader(value = "traceId", required = true) String traceId,
            @RequestHeader(value = "video_id", required = true) UUID videoId) {

        Map<String, String> response = new HashMap<>();
        response.put("video_id", videoId.toString());
        response.put("interaction_count", String.valueOf(interactionsService.getInteractionCount(videoId)));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/event")
    public ResponseEntity<Map<String, String>> getInteractionCountByEvent(
            @RequestHeader(value = "traceId", required = true) String traceId,
            @RequestHeader(value = "video_id", required = true) UUID videoId,
            @RequestHeader(value = "event_type", required = true) VideoInteractionEventType eventType) {

        Map<String, String> response = new HashMap<>();
        response.put("video_id", videoId.toString());
        response.put("event_type", eventType.toString());
        response.put("interaction_count", String.valueOf(interactionsService.getInteractionCountByEvent(videoId,eventType)));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/user")
    public ResponseEntity<Map<String, String>> getUserInteractionCountByEvent(
            @RequestHeader(value = "traceId", required = true) String traceId,
            @RequestHeader(value = "user_id", required = true) Long userId,
            @RequestHeader(value = "event_type", required = true) VideoInteractionEventType eventType) {

        Map<String, String> response = new HashMap<>();
        response.put("user_id", userId.toString());
        response.put("event_type", eventType.toString());
        response.put("interaction_count", String.valueOf(interactionsService.getUserInteractionCountByEvent(userId,eventType)));

        return ResponseEntity.ok(response);
    }
}