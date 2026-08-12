package com.biy.social.curvydolphin.exceptions;

import com.biy.social.curvydolphin.entity.LikesEntity;

import java.util.UUID;

public class LikesException extends RuntimeException{
    public LikesException(String message){
        super(message);
    }

    public LikesException(UUID videoId, Long userId){
        super("Like not found for video [" + videoId + "] from user [" + userId + "]");
    }
}
