package org.usermanagement.usermanagement.exception;


import org.usermanagement.usermanagement.enums.ErrorCode;

public class DuplicateEntityException extends BaseException {
    public DuplicateEntityException(String message) {
        super(ErrorCode.DUPLICATE_ENTITY, message);
    }

    public DuplicateEntityException() {
        super(ErrorCode.DUPLICATE_ENTITY);
    }
}
