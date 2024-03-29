package org.godseop.cherry.core.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = -761503632186596342L;

    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }

}
