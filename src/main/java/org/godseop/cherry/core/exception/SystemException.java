package org.godseop.cherry.core.exception;

public class SystemException extends RuntimeException {
    public SystemException(Exception exception) {
        super(exception);
    }
}
