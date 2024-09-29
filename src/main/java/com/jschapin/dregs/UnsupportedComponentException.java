package com.jschapin.dregs;

public class UnsupportedComponentException extends RuntimeException {
    public UnsupportedComponentException(String message) {
        super(message);
    }

    public UnsupportedComponentException(Throwable cause) {
        super(cause);
    }
}
