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
import com.mycontactsapp.contact.Contact;


public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private UserType userType;
    private com.mycontactsapp.profile.UserPreferences preferences;
    private com.mycontactsapp.contact.Contact.ContactBook contactBook;

    private User(String firstName, String lastName, String email, String passwordHash, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.preferences = new com.mycontactsapp.profile.UserPreferences();
        this.contactBook = new com.mycontactsapp.contact.Contact.ContactBook();
    }

    public static User register(String firstName, String lastName, String email, String password, UserType userType)
            throws InvalidInputException {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        if (firstName == null || firstName.isEmpty()) {
            throw new InvalidInputException("First name is required.");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new InvalidInputException("Last name is required.");
        }
        String normalizedEmail = emailValidator.validate(email);
        String validatedPassword = passwordValidator.validate(password);
        UserType resolvedUserType = Objects.requireNonNullElse(userType, UserType.FREE);

        String passwordHash = passwordValidator.hashSimple(validatedPassword);
        return new User(firstName, lastName, normalizedEmail, passwordHash, resolvedUserType);
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

    public com.mycontactsapp.contact.Contact.ContactBook getContactBook() {
        return contactBook;
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

    public void addContact(Contact contact) throws InvalidInputException {
        if (contactBook == null) {
            contactBook = new com.mycontactsapp.contact.Contact.ContactBook();
        }
        contactBook.addContact(contact);
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
        if (firstName == null || firstName.isEmpty()) {
            throw new InvalidInputException("First name is required.");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws InvalidInputException {
        if (lastName == null || lastName.isEmpty()) {
            throw new InvalidInputException("Last name is required.");
        }
        this.lastName = lastName;
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
}