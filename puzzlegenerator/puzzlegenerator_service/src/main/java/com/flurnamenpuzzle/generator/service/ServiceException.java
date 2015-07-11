package com.flurnamenpuzzle.generator.service;

/**
 * This class represents an error that occurred in a service instance.
 */
public class ServiceException extends RuntimeException {

	/**
	 * Creates a new instance.
	 * 
	 * @param exception
	 *            The actual {@link Exception} that occurred.
	 * @param message
	 *            A message providing additional information about the error
	 *            that occurred.
	 */
	public ServiceException(Exception exception, String message) {
		super(message, exception);
	}
}
