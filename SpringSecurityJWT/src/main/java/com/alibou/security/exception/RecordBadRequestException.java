package com.alibou.security.exception;

public class RecordBadRequestException extends RuntimeException{
    public RecordBadRequestException() {
        super();
    }

    public RecordBadRequestException(String message) {
        super(message);
    }
}
