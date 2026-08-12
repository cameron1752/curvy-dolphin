package com.biy.social.curvydolphin.exceptions;

import java.util.UUID;

public class VideoInteractionsException extends RuntimeException{
    public VideoInteractionsException(long id){
        super("Video interaction with id [" + id + "] not found");
    }
    public VideoInteractionsException(String message){ super(message);}
}
