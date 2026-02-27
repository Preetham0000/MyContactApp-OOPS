package com.mycontactsapp.validation;

import java.util.regex.Pattern;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;

/**
 * Use Case 1: User Registration
 * This class is responsible for:
 * - Validating email format with a simple regex
 * - Normalizing email input
 *
 * Demonstrates:
 * - Input validation
 * - Regular expressions
 */
public class EmailValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public String validate(String email) throws InvalidInputException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email is required.");
        }
        String normalized = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new InvalidInputException("Email format is invalid.");
        }
        return normalized;
    }
}
