package com.xiaowenz.scv.core;

public class InvalidVersionException extends Exception{

    public InvalidVersionException() {
        super();
    }

    public InvalidVersionException(String message) {
        super(message);
    }

    public InvalidVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
