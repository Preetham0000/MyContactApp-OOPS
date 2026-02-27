/**
 * MyContactApp - Use Case 1 and 2: Registration and Authentication
 *
 * This class serves as the application entry point.
 * It demonstrates basic registration with validation and simple authentication.
 *
 * No persistence or contact list logic is implemented at this stage.
 *
 * @author Developer
 * @version 2.0
 */

package com.mycontactsapp.main;


import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.auth.AuthenticationStrategy;
import com.mycontactsapp.auth.BasicAuth;
import com.mycontactsapp.user.User;
import com.mycontactsapp.user.UserType;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<User> registeredUsers = new ArrayList<>();
        AuthenticationStrategy authStrategy = new BasicAuth();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
            	System.out.println("\nMy Contact App");
                System.out.print("Choose action (signup/login/exit): ");
                String action = scanner.nextLine().trim().toLowerCase();

                if ("signup".equals(action)) {
                    handleSignup(scanner, registeredUsers);
                } else if ("login".equals(action)) {
                    handleLogin(scanner, registeredUsers, authStrategy);
                } else if ("exit".equals(action)) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid option. Please enter signup, login, or exit.");
                }
            }
        }
    }

    private static void handleSignup(Scanner scanner, List<User> registeredUsers) {
        try {
            System.out.print("Enter user type (free/premium): ");
            String userTypeInput = scanner.nextLine();
            UserType userType = parseUserType(userTypeInput);

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = User.register(firstName, lastName, email, password, userType);
            registeredUsers.add(user);
            System.out.println("User created: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role: " + user.getUserType());
        } catch (InvalidInputException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void handleLogin(Scanner scanner, List<User> registeredUsers, AuthenticationStrategy authStrategy) {
        if (registeredUsers.isEmpty()) {
            System.out.println("No registered users. Please sign up first.");
            return;
        }

        System.out.print("Login email: ");
        String loginEmail = scanner.nextLine();

        System.out.print("Login password: ");
        String loginPassword = scanner.nextLine();

        User registeredUser = findUserByEmail(registeredUsers, loginEmail);
        Optional<User> loginResult = authStrategy.authenticate(loginEmail, loginPassword, registeredUser);
        if (loginResult.isPresent()) {
            System.out.println("Login successful. Welcome, " + loginResult.get().getFirstName() + "!");
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private static User findUserByEmail(List<User> registeredUsers, String email) {
        if (email == null) {
            return null;
        }
        for (User user : registeredUsers) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    private static UserType parseUserType(String input) throws InvalidInputException {
        if (input == null) {
            throw new InvalidInputException("User type is required.");
        }
        String normalized = input.trim().toUpperCase();
        switch (normalized) {
            case "FREE":
                return UserType.FREE;
            case "PREMIUM":
                return UserType.PREMIUM;
            default:
                throw new InvalidInputException("User Type is either FREE or PREMIUM");
        }
    }
}