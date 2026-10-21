package com.biy.social.curvydolphin.repository;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.model.User;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, User> {

    public Optional<UserEntity> getByUserId(long id);

    @Query("""
    SELECT u
    FROM UserEntity u
""")
    public List<UserEntity> getAllUsers();
    public Optional<UserEntity> findByProviderId(String id);
}
