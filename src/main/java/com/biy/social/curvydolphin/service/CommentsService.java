package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.CommentsEntity;
import com.biy.social.curvydolphin.exceptions.CommentsException;
import com.biy.social.curvydolphin.model.Comment;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.repository.CommentsRepository;
import com.biy.social.curvydolphin.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentsService {

    @Autowired
    CommentsRepository commentRepository;

    @Autowired
    AuthorizationService authorizationService;

    public Comment getComment(long id) {
        Optional<CommentsEntity> entity = commentRepository.findById(id);

        if (entity.isEmpty()){
            throw new CommentsException(id);
        } else {
            return Comment.fromEntity(entity.get());
        }
    }

    // get video's comments
    public List<Comment> getCommentsByVideo(UUID videoId) {
        return commentRepository
                .findByVideoIdOrderByCreatedAtDesc(videoId)
                .stream()
                .map(Comment::fromEntity)
                .toList();
    }

    // get users comments
    public List<Comment> getCommentsByUser(Long userId) {
        return commentRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(Comment::fromEntity)
                .toList();
    }

    public Comment createComment(Comment comment) {
        User user = authorizationService.getCurrentAccount();

        comment.setUserId(user);

        CommentsEntity entity = comment.toEntity();
        entity.setId(null);
        entity.setCreatedAt(LocalDateTime.now());
        CommentsEntity savedEntity = commentRepository.save(entity);

        return Comment.fromEntity(savedEntity);
    }


    public Comment updateComment(Long id, Comment comment) {
        Optional<CommentsEntity> existingEntity = commentRepository.findById(id);

        if (existingEntity.isEmpty()){
            throw new CommentsException(id);
        } else {
            existingEntity.get().setText(comment.getText());

            CommentsEntity savedEntity =
                    commentRepository.save(existingEntity.get());

            return Comment.fromEntity(savedEntity);
        }
    }

    public void deleteComment(Long id) {
        Optional<CommentsEntity> existingEntity = commentRepository.findById(id);

        if (existingEntity.isEmpty()){
            throw new CommentsException(id);
        } else {
            commentRepository.delete(existingEntity.get());
        }
    }

    public long getCommentCount(UUID videoId) {
        return commentRepository.countByVideoId(videoId);
    }
}
