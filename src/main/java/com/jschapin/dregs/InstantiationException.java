package com.jschapin.dregs;

public class InstantiationException extends RuntimeException {
    public InstantiationException(String message) {
        super(message);
    }

    public InstantiationException(Throwable cause) {
        super(cause);
    }
}
