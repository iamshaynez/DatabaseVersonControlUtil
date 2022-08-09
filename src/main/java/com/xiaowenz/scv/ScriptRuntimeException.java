package com.xiaowenz.scv;

public class ScriptRuntimeException extends Exception{

    public ScriptRuntimeException() {
        super();
    }

    public ScriptRuntimeException(String message) {
        super(message);
    }

    public ScriptRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptRuntimeException(Exception e) {
        super(e);
    }
}
