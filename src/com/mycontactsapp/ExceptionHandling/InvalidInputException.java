package com.mycontactsapp.ExceptionHandling;

/**
 * Use Case 1: User Registration
 * This class is responsible for:
 * - Representing validation errors during registration
 * - Providing clear error messages for invalid input
 *
 * Demonstrates:
 * - Exception handling
 */
public class InvalidInputException extends Exception {
	
    public InvalidInputException(String message) {
        super(message);
    }
}