package com.mycontactsapp.user;

import java.util.Objects;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.validation.EmailValidator;
import com.mycontactsapp.validation.PasswordValidator;

/**
 * Use Case 1: User Registration
 * This class is responsible for:
 * - Encapsulating user profile data
 * - Validating registration inputs
 * - Hashing user passwords
 *
 * Demonstrates:
 * - Encapsulation
 * - Input validation
 * - Password hashing
 */
public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String passwordHash;
    private final UserType userType;

    private User(String firstName, String lastName, String email, String passwordHash, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
    }

    public static User register(String firstName, String lastName, String email, String password, UserType userType)
            throws InvalidInputException {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        String sanitizedFirstName = requireNonBlank(firstName, "First name is required.");
        String sanitizedLastName = requireNonBlank(lastName, "Last name is required.");
        String normalizedEmail = emailValidator.validate(email);
        String validatedPassword = passwordValidator.validate(password);
        UserType resolvedUserType = Objects.requireNonNullElse(userType, UserType.FREE);

        String passwordHash = passwordValidator.hashSimple(validatedPassword);
        return new User(sanitizedFirstName, sanitizedLastName, normalizedEmail, passwordHash, resolvedUserType);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean verifyPassword(String password) throws InvalidInputException {
        PasswordValidator passwordValidator = new PasswordValidator();
        String validatedPassword = passwordValidator.validate(password);
        return passwordHash.equals(passwordValidator.hashSimple(validatedPassword));
    }

    private static String requireNonBlank(String value, String message) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(message);
        }
        return value.trim();
    }
}