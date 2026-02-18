package org.usermanagement.usermanagement.exception;

import lombok.Getter;
import org.usermanagement.usermanagement.enums.ErrorCode;

@Getter
public class BaseException extends RuntimeException {

    public final ErrorCode errorCode;

    public BaseException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
