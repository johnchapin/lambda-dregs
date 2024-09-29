package com.jschapin.dregs;

public class RecordCreationException extends RuntimeException {
    public RecordCreationException(String message) {
        super(message);
    }

    public RecordCreationException(Throwable cause) {
        super(cause);
    }
}
