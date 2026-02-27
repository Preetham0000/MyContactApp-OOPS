package com.mycontactsapp.main;

/**
 * MyContactApp - Use Case 1: User Registration
 *
 * This class serves as the application entry point.
 * It demonstrates basic registration with validation and password hashing.
 *
 * No persistence or login flow is implemented at this stage.
 *
 * @author Developer
 * @version 1.0
 */


import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.user.User;
import com.mycontactsapp.user.UserType;
import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
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
            System.out.println("User created: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role: " + user.getUserType());

            boolean verified = user.verifyPassword(password);
            System.out.println("Password verified: " + verified);
        } catch (InvalidInputException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
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
                throw new InvalidInputException("User type must be 'free' or 'premium'.");
        }
    }
}