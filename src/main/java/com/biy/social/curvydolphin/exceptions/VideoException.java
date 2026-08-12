package com.biy.social.curvydolphin.exceptions;

import java.util.UUID;

public class VideoException extends RuntimeException{

    public VideoException(UUID id){
        super("Video with id [" + id + "] not found");
    }
    public VideoException(String message){ super(message);}

}
