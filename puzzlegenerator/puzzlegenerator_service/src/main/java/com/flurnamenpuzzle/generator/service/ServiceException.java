package com.flurnamenpuzzle.generator.service;

/**
 * This class represents an error that occurred in a service instance.
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

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

	/**
	 * Creates a new instance.
	 * 
	 * @param message
	 *            A message providing information about the error that occurred.
	 */
	public ServiceException(String message) {
		super(message);
	}
}
