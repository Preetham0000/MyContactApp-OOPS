/**
 * UC 1: User Registration
 * - Encapsulating user profile data
 * - Validating input during registration
 * - Hashing user passwords
 * - Encapsulation
 * - Input validation
 * - Password hashing
 */

package com.mycontactsapp.user;
import java.util.*;


import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.validation.EmailValidator;
import com.mycontactsapp.validation.PasswordValidator;


public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private UserType userType;
    private com.mycontactsapp.profile.UserPreferences preferences;

    private User(String firstName, String lastName, String email, String passwordHash, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.preferences = new com.mycontactsapp.profile.UserPreferences();
    }

    public static User register(String firstName, String lastName, String email, String password, UserType userType)
            throws InvalidInputException {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        String sanitizedFirstName = NonBlank(firstName, "First name is required.");
        String sanitizedLastName = NonBlank(lastName, "Last name is required.");
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

    public com.mycontactsapp.profile.UserPreferences getPreferences() {
        return preferences;
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean verifyPassword(String password) throws InvalidInputException {
        PasswordValidator passwordValidator = new PasswordValidator();
        String validatedPassword = passwordValidator.validate(password);
        return passwordHash.equals(passwordValidator.hashSimple(validatedPassword));
    }

    public void updateProfile(String firstName, String lastName, String email) throws InvalidInputException {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public void changePassword(String currentPassword, String newPassword) throws InvalidInputException {
        if (!verifyPassword(currentPassword)) {
            throw new InvalidInputException("Current password is incorrect.");
        }
        setPassword(newPassword);
    }

    public void updatePreferences(boolean emailNotifications) throws InvalidInputException {
        if (preferences == null) {
            preferences = new com.mycontactsapp.profile.UserPreferences();
        }
        preferences.setEmailNotifications(emailNotifications);
        preferences.setPreferredUserType(userType);
    }

    public void setUserType(UserType userType) throws InvalidInputException {
        if (userType == null) {
            throw new InvalidInputException("User type is required.");
        }
        this.userType = userType;
        if (preferences != null) {
            preferences.setPreferredUserType(userType);
        }
    }

    public UserType toggleUserType() throws InvalidInputException {
        UserType nextType = (this.userType == UserType.PREMIUM) ? UserType.FREE : UserType.PREMIUM;
        setUserType(nextType);
        return nextType;
    }

    public void setFirstName(String firstName) throws InvalidInputException {
        this.firstName = NonBlank(firstName, "First name is required.");
    }

    public void setLastName(String lastName) throws InvalidInputException {
        this.lastName = NonBlank(lastName, "Last name is required.");
    }

    public void setEmail(String email) throws InvalidInputException {
        EmailValidator emailValidator = new EmailValidator();
        this.email = emailValidator.validate(email);
    }

    private void setPassword(String password) throws InvalidInputException {
        PasswordValidator passwordValidator = new PasswordValidator();
        String validatedPassword = passwordValidator.validate(password);
        this.passwordHash = passwordValidator.hashSimple(validatedPassword);
    }

    private static String NonBlank(String value, String message) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(message);
        }
        return value.trim();
    }
}