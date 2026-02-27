package com.mycontactsapp.validation;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;

/**
 * Use Case 1: User Registration
 * This class is responsible for:
 * - Validating password rules
 * - Creating a simple hashed representation
 *
 * Demonstrates:
 * - Input validation
 * - Basic hashing logic
 */
public class PasswordValidator {
    public String validate(String password) throws InvalidInputException {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password is required.");
        }
        if (password.length() < 2) {
            throw new InvalidInputException("Password must be at least 8 characters.");
        }
        return password;
    }

    public String hashSimple(String password) {
        int hash = 0;
        for (int i = 0; i < password.length(); i++) {
            hash = (hash * 31) + password.charAt(i);
        }
        return Integer.toHexString(hash);
    }
}
