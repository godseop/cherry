package org.godseop.cherry.core.exception;

import org.godseop.cherry.core.model.Error;

public class CherryException extends RuntimeException {

    private String code;

    public CherryException(Error error) {
        super(error.getMessage());
        this.code = error.getCode();
    }

    public String getCode() {
        return this.code;
    }

}
