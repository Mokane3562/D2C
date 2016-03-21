package com.d2c.util;

public class EmptySetException extends Exception {
	public EmptySetException() {
        super();
    }
	public EmptySetException(String message) {
        super(message);
    }
}