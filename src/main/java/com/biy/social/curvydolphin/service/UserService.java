package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.exceptions.UserException;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUserById(long id) throws UserException {
        Optional<UserEntity> userEntity = userRepository.getByUserId(id);

        if (userEntity.isEmpty()){
            throw new UserException(id);
        } else {
            return User.fromEntity(userEntity.get());
        }
    }

    public User getByProviderId(String provider_id) throws UserException{
        Optional<UserEntity> userEntity = userRepository.findByProviderId(provider_id);

        if (userEntity.isEmpty()){
            throw new UserException(provider_id);
        } else {
            return User.fromEntity(userEntity.get());
        }
    }

    public User createUser(User user) {
        UserEntity saved = userRepository.save(user.toEntity());

        return User.fromEntity(saved);
    }

    public User updateUser(long id, User user) {
        Optional<UserEntity> userEntity = userRepository.getByUserId(id);

        if (userEntity.isEmpty()){
            throw new UserException(id);
        } else {
            UserEntity entity = userEntity.get();

            entity.setUsername(user.getUsername());
            entity.setName(user.getName());
            entity.setEmail(user.getEmail());
            entity.setAvatarUrl(user.getAvatar_url());

            UserEntity saved = userRepository.save(entity);
            return User.fromEntity(saved);
        }
    }

    public void deleteUser(long id){
        Optional<UserEntity> userEntity = userRepository.getByUserId(id);

        if (userEntity.isEmpty()){
            throw new UserException(id);
        } else {
            userRepository.delete(userEntity.get());
        }
    }
}
