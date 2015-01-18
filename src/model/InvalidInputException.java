package model;

/**
 * This Exception indicates that input, from an HTTP request body, parameter or otherwise, was invalid.
 * 
 */
public class InvalidInputException extends Exception {

	public InvalidInputException(String errorMessage) {
		super(errorMessage);
	}
	
}
