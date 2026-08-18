package com.biy.social.curvydolphin.model;

import com.biy.social.curvydolphin.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long user_id;
    private String providerId;
    private String username;
    private String email;
    private String name;
    private String avatar_url;
    private LocalDateTime created_at;

    public static User fromEntity(UserEntity userEntity){
        return new User(userEntity.getUserId(),
                userEntity.getProviderId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getAvatarUrl(),
                userEntity.getCreatedAt());
    }

    public UserEntity toEntity(){
        UserEntity entity = new UserEntity();

        entity.setProviderId(this.providerId);
        entity.setUsername(this.username);
        entity.setEmail(this.email);
        entity.setName(this.name);
        entity.setAvatarUrl(this.avatar_url);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }
}
