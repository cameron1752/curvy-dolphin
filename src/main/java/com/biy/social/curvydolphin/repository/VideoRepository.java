package com.biy.social.curvydolphin.repository;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoEntity, Video> {
    List<VideoEntity> getAllByUser(UserEntity user);
    Optional<VideoEntity> getById(UUID id);

    @Transactional
    void deleteById(UUID id);
}
