package com.desmart.common.exception;

public class BpmFindNextNodeException extends PlatformException{


    public BpmFindNextNodeException(String message) {
        super(message);
    }

    public BpmFindNextNodeException(String message, Throwable cause) {
        super(message, cause);
    }
}