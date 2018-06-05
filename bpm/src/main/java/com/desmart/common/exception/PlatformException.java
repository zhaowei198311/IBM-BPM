package com.desmart.common.exception;

/**
 * 平台统一异常类
 */
public class PlatformException extends RuntimeException {

    public PlatformException(String message, Throwable cause) {
        super(message, cause);

    }

    public PlatformException(String message) {
        super(message);

    }

}
