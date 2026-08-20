package com.biy.social.curvydolphin.repository;

import com.biy.social.curvydolphin.entity.CommentsEntity;
import com.biy.social.curvydolphin.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository extends JpaRepository<CommentsEntity, Comment> {

    Optional<CommentsEntity> findById(long id);
    List<CommentsEntity> findByVideoIdOrderByCreatedAtDesc(UUID videoId);

    List<CommentsEntity> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    long countByVideoId(UUID videoId);
}