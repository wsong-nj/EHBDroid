package com.app.test.exception;

public class ClassIsNotActivityException extends RuntimeException{
	public ClassIsNotActivityException(Exception ex) {
        super(ex);
    }
    public ClassIsNotActivityException(String message) {
        super(message);
    }
}
