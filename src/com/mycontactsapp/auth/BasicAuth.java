
/**
 * Use Case 2: User Authentication
 * This class is responsible for:
 * - Authenticating with email and password
 *
 * Demonstrates:
 * - Strategy implementation
 */
package com.mycontactsapp.auth;

import java.util.*;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.user.User;


public class BasicAuth implements AuthenticationStrategy {
    @Override
    public Optional<User> authenticate(String email, String password, User registeredUser) {
        if (registeredUser == null || email == null) {
            return Optional.empty();
        }
        if (!registeredUser.getEmail().equalsIgnoreCase(email)) {
            return Optional.empty();
        }
        try {
            if (registeredUser.verifyPassword(password)) {
                return Optional.of(registeredUser);
            }
        } catch (InvalidInputException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
