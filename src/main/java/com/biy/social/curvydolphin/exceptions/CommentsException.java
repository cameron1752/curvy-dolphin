package com.biy.social.curvydolphin.exceptions;

import java.util.UUID;

public class CommentsException extends RuntimeException {
    public CommentsException(long id){
        super("Comment with id [" + id + "] not found");
    }
    public CommentsException(String message){ super(message);}
}
