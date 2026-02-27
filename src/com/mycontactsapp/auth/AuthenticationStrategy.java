package com.mycontactsapp.auth;

import java.util.*;

import com.mycontactsapp.user.User;

/**
 * Use Case 2: User Authentication
 * This interface is responsible for:
 * - Defining a simple authentication contract
 *
 * Demonstrates:
 * - Basic polymorphism
 */
public interface AuthenticationStrategy {
    Optional<User> authenticate(String email, String password, User registeredUser);
}
