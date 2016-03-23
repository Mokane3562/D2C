package com.d2c.util;

/**
 * This exception is used to notify a user of the system or the api of when a select statement returns the empty set.
 */
public class EmptySetException extends Exception {
	/**
	 * Creates a new EmptySetException.
	 */
	public EmptySetException() {
        super();
    }
	/**
	 * Creates a new EmptySetException with a message.
	 * 
	 * @param message the reason why this exception was thrown
	 */
	public EmptySetException(String message) {
        super(message);
    }
}