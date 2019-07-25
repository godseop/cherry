package org.godseop.cherry.core.exception;

import org.godseop.cherry.core.model.Error;

public class CherryException extends RuntimeException {

    private Error error;

    public CherryException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public Error getError() {
        return this.error;
    }

}
