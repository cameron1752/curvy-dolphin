package com.biy.social.curvydolphin.repository;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoEntity, Video> {
    List<VideoEntity> getAllByUser(UserEntity user);
    Optional<VideoEntity> getById(UUID id);

    @Query("""
    SELECT v
    FROM VideoEntity v
    WHERE v.user.userId <> :userId
    ORDER BY v.createdAt DESC
""")
    List<VideoEntity> findVideosNotByUser(@Param("userId") Long userId);

    @Transactional
    void deleteById(UUID id);
}
