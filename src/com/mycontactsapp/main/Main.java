/**
 * MyContactApp - Use Case 1,2 and 3: Registration, Authentication and Profile Management
 
 * It demonstrates basic registration with validation and simple authentication.
 * It also allows users to manage their profile and preferences.
 * 
 *
 * @author Developer
 * @version 3.0
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
            handleProfileMenu(scanner, loginResult.get());
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private static void handleProfileMenu(Scanner scanner, User user) {
        while (true) {
            System.out.println("\nProfile Management");
            System.out.print("Choose action (update-profile/change-password/preferences/logout): ");
            String action = scanner.nextLine().trim().toLowerCase();

            if ("update-profile".equals(action)) {
                updateProfile(scanner, user);
            } else if ("change-password".equals(action)) {
                changePassword(scanner, user);
            } else if ("preferences".equals(action)) {
                updatePreferences(scanner, user);
            } else if ("logout".equals(action)) {
                System.out.println("Logged out.");
                break;
            } else {
                System.out.println("Invalid option. Please enter update-profile, change-password, preferences, or logout.");
            }
        }
    }

    private static void updateProfile(Scanner scanner, User user) {
        try {
            System.out.print("Update first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Update last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Update email: ");
            String email = scanner.nextLine();

            user.updateProfile(firstName, lastName, email);
            System.out.println("Profile updated.");
            System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Email: " + user.getEmail());
        } catch (InvalidInputException e) {
            System.out.println("Profile update failed: " + e.getMessage());
        }
    }

    private static void changePassword(Scanner scanner, User user) {
        try {
            System.out.print("Current password: ");
            String currentPassword = scanner.nextLine();

            System.out.print("New password: ");
            String newPassword = scanner.nextLine();

            user.changePassword(currentPassword, newPassword);
            System.out.println("Password updated.");
        } catch (InvalidInputException e) {
            System.out.println("Password change failed: " + e.getMessage());
        }
    }

    private static void updatePreferences(Scanner scanner, User user) {
        try {
            System.out.print("Enable email notifications (yes/no): ");
            boolean emailNotifications = readYesNo(scanner.nextLine());

            System.out.print("Switch user type (yes/no): ");
            boolean switchUserType = readYesNo(scanner.nextLine());

            if (switchUserType) {
                UserType newType = user.toggleUserType();
                System.out.println("User type switched to: " + newType);
            }

            user.updatePreferences(emailNotifications);
            System.out.println("Preferences updated: " + user.getPreferences());
        } catch (InvalidInputException e) {
            System.out.println("Preferences update failed: " + e.getMessage());
        }
    }

    private static boolean readYesNo(String input) throws InvalidInputException {
        if (input == null) {
            throw new InvalidInputException("Response is required.");
        }
        String normalized = input.trim().toLowerCase();
        if ("yes".equals(normalized) || "y".equals(normalized)) {
            return true;
        }
        if ("no".equals(normalized) || "n".equals(normalized)) {
            return false;
        }
        throw new InvalidInputException("Please respond with yes or no.");
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