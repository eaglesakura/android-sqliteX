package com.eaglesakura.android.sqlite;

public class CursorCanceledException extends RuntimeException {
    public CursorCanceledException() {
    }

    public CursorCanceledException(String message) {
        super(message);
    }

    public CursorCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public CursorCanceledException(Throwable cause) {
        super(cause);
    }
}
