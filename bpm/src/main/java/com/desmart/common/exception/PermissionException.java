package com.desmart.common.exception;

import com.desmart.common.exception.PlatformException;

/**
 * 权限异常
 * @author yaoyunqing
 *
 */
public class PermissionException extends PlatformException {

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(String message) {
        super(message);
    }



}
