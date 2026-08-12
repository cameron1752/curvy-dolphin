package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.exceptions.CommentsException;
import com.biy.social.curvydolphin.model.Comment;
import com.biy.social.curvydolphin.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@RequestHeader(value = "traceId", required = true) String traceId,
                                               @RequestHeader(value = "comment_id", required = false) Long comment_id,
                                               @RequestHeader(value = "video_id", required = false) UUID video_id,
                                               @RequestHeader(value = "user_id", required = false) Long user_id) {

        if (comment_id != null ){
            return ResponseEntity.ok(
                    Collections.singletonList(commentsService.getComment(comment_id))
            );
        }
        if (video_id != null){
            return ResponseEntity.ok(
                    commentsService.getCommentsByVideo(video_id)
            );
        }
        if (user_id != null){
            return ResponseEntity.ok(
                    commentsService.getCommentsByUser(user_id)
            );
        }

        throw new CommentsException("comment_id, video_id, or user_id required");
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, String>> getCommentCount(
            @RequestHeader(value = "video_id", required = true) UUID videoId) {
        Map<String, String> response = new HashMap<>();
        response.put("video_id", videoId.toString());
        response.put("count", String.valueOf(commentsService.getCommentCount(videoId)));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody Comment comment) {

        Comment createdComment =
                commentsService.createComment(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdComment);
    }

    @PatchMapping
    public ResponseEntity<Comment> updateComment(
            @RequestHeader(value = "comment_id", required = true) Long id,
            @RequestBody Comment comment) {

        return ResponseEntity.ok(
                commentsService.updateComment(id, comment)
        );
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteComment(
            @RequestHeader(value = "comment_id", required = true) Long id) {

        commentsService.deleteComment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comment deleted with Id: " + id);
        return ResponseEntity.ok(response);
    }
}