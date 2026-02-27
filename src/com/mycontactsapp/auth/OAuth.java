/**
 * Use Case 2: User Authentication
 * This implements a simple OAuth-like authentication strategy.
 * - Verifying a user password as a simple OAuth stand-in.
 
 */

package com.mycontactsapp.auth;

import java.util.*;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.user.User;


public class OAuth implements AuthenticationStrategy {
    @Override
    public Optional<User> authenticate(String email, String password, User registeredUser) {
        if (registeredUser == null) {
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
