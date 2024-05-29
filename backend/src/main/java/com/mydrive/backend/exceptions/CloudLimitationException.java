package com.mydrive.backend.exceptions;

public class CloudLimitationException extends RuntimeException {

    public CloudLimitationException() {
        super();
    }

    public CloudLimitationException(String message) {
        super(message);
    }

    public CloudLimitationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudLimitationException(Throwable cause) {
        super(cause);
    }
}
