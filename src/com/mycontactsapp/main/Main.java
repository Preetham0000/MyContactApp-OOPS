/**
 * MyContactApp - Use Case 1,2,3 and 4: Registration, Authentication, Profile Management and Contact Creation
 * 
 * It demonstrates basic registration with validation and simple authentication.
 * It also allows users to manage their profile, preferences, and contacts.
 * 
 *
 * @author Developer
 * @version 6.0
 */

package com.mycontactsapp.main;


import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.auth.AuthenticationStrategy;
import com.mycontactsapp.auth.BasicAuth;
import com.mycontactsapp.contact.Contact;
import com.mycontactsapp.user.User;
import com.mycontactsapp.user.UserType;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<User> registeredUsers = new ArrayList<>();
        AuthenticationStrategy authStrategy = new BasicAuth();

        try (Scanner scanner = new Scanner(System.in)) {
            // Main menu loop
            while (true) {
                System.out.println("\nMy Contact App");
                System.out.print("Choose action (signup/login/exit): ");
                String action = scanner.nextLine().toLowerCase();

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
        // Logged-in user actions
        while (true) {
            System.out.println("\nProfile Management");
            System.out.print("Choose action (update-profile/change-password/preferences/Create Contact/View Contact/Edit Contact/logout): ");
            String action = scanner.nextLine().toLowerCase();

            if ("update-profile".equals(action)) {
                updateProfile(scanner, user);
            } else if ("change-password".equals(action)) {
                changePassword(scanner, user);
            } else if ("preferences".equals(action)) {
                updatePreferences(scanner, user);
            } else if ("create contact".equals(action) || "create-contact".equals(action)) {
                createContact(scanner, user);
            } else if ("view contact".equals(action) || "view-contact".equals(action)) {
                viewContactDetails(scanner, user);
            } else if ("edit contact".equals(action) || "edit-contact".equals(action)) {
                editContact(scanner, user);
            } else if ("logout".equals(action)) {
                System.out.println("Logged out.");
                break;
            } else {
                System.out.println("Invalid option. Please enter update-profile, change-password, preferences, Create Contact, View Contact, Edit Contact, or logout.");
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

    private static void createContact(Scanner scanner, User user) {
        try {
            System.out.print("Contact type (person/organization): ");
            String typeInput = scanner.nextLine().toLowerCase();
            Contact contact;

            if ("person".equals(typeInput)) {
                System.out.print("First name: ");
                String firstName = scanner.nextLine();
                System.out.print("Last name: ");
                String lastName = scanner.nextLine();
                contact = new Contact.PersonContact(firstName, lastName);
            } else if ("organization".equals(typeInput)) {
                System.out.print("Organization name: ");
                String orgName = scanner.nextLine();
                System.out.print("Contact person (optional): ");
                String contactPerson = scanner.nextLine();
                contact = new Contact.OrganizationContact(orgName, contactPerson);
            } else {
                System.out.println("Invalid contact type. Please enter person or organization.");
                return;
            }

            // Add multiple phone numbers
            while (true) {
                System.out.print("Add phone label (or press Enter to stop): ");
                String label = scanner.nextLine();
                if (label.isEmpty()) {
                    break;
                }
                System.out.print("Phone number: ");
                String number = scanner.nextLine();
                contact.addPhoneNumber(new Contact.PhoneNumber(label, number));
            }

            // Add multiple email addresses
            while (true) {
                System.out.print("Add email label (or press Enter to stop): ");
                String label = scanner.nextLine();
                if (label.isEmpty()) {
                    break;
                }
                System.out.print("Email address: ");
                String address = scanner.nextLine();
                contact.addEmailAddress(new Contact.EmailAddress(label, address));
            }

            // Optional fields
            while (true) {
                System.out.print("Add optional field name (or press Enter to stop): ");
                String key = scanner.nextLine();
                if (key.isEmpty()) {
                    break;
                }
                System.out.print("Field value: ");
                String value = scanner.nextLine();
                contact.addOptionalField(key, value);
            }

            user.addContact(contact);
            System.out.println("Contact created with ID: " + contact.getId());
        } catch (InvalidInputException e) {
            System.out.println("Contact creation failed: " + e.getMessage());
        }
    }

    private static void viewContactDetails(Scanner scanner, User user) {
        if (user.getContactBook().getContacts().isEmpty()) {
            System.out.println("No contacts saved yet.");
            return;
        }

        System.out.println("Saved contacts:");
        for (Contact contact : user.getContactBook().getContacts()) {
            System.out.println(contact.getId() + " - " + contact.getDisplayName());
        }

        System.out.print("Enter contact ID to view details: ");
        String idInput = scanner.nextLine();
        if (idInput.isEmpty()) {
            System.out.println("Contact ID is required.");
            return;
        }

        Optional<Contact> found = findContactById(user.getContactBook().getContacts(), idInput);
        if (found.isPresent()) {
            System.out.println(found.get().toView());
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void editContact(Scanner scanner, User user) {
        if (user.getContactBook().getContacts().isEmpty()) {
            System.out.println("No contacts saved yet.");
            return;
        }

        System.out.println("Saved contacts:");
        for (Contact contact : user.getContactBook().getContacts()) {
            System.out.println(contact.getId() + " - " + contact.getDisplayName());
        }

        System.out.print("Enter contact ID to edit: ");
        String idInput = scanner.nextLine();
        if (idInput.isEmpty()) {
            System.out.println("Contact ID is required.");
            return;
        }

        Optional<Contact> found = user.getContactBook().findById(idInput);
        if (!found.isPresent()) {
            System.out.println("Contact not found.");
            return;
        }

        try {
            Contact current = found.get();
            Contact updated;

            if (current instanceof Contact.PersonContact) {
                Contact.PersonContact person = (Contact.PersonContact) current;
                Contact.PersonContact copy = new Contact.PersonContact(person);

                System.out.print("New first name (leave blank to keep): ");
                String firstName = scanner.nextLine();
                if (!firstName.isEmpty()) {
                    copy.setFirstName(firstName);
                }

                System.out.print("New last name (leave blank to keep): ");
                String lastName = scanner.nextLine();
                if (!lastName.isEmpty()) {
                    copy.setLastName(lastName);
                }

                updated = copy;
            } else if (current instanceof Contact.OrganizationContact) {
                Contact.OrganizationContact org = (Contact.OrganizationContact) current;
                Contact.OrganizationContact copy = new Contact.OrganizationContact(org);

                System.out.print("New organization name (leave blank to keep): ");
                String orgName = scanner.nextLine();
                if (!orgName.isEmpty()) {
                    copy.setOrganizationName(orgName);
                }

                System.out.print("New contact person (leave blank to keep): ");
                String contactPerson = scanner.nextLine();
                if (!contactPerson.isEmpty()) {
                    copy.setContactPerson(contactPerson);
                }

                updated = copy;
            } else {
                System.out.println("Unsupported contact type.");
                return;
            }

            System.out.print("Add another phone (yes/no): ");
            if (readYesNo(scanner.nextLine())) {
                System.out.print("Phone label: ");
                String label = scanner.nextLine();
                System.out.print("Phone number: ");
                String number = scanner.nextLine();
                updated.addPhoneNumber(new Contact.PhoneNumber(label, number));
            }

            System.out.print("Add another email (yes/no): ");
            if (readYesNo(scanner.nextLine())) {
                System.out.print("Email label: ");
                String label = scanner.nextLine();
                System.out.print("Email address: ");
                String address = scanner.nextLine();
                updated.addEmailAddress(new Contact.EmailAddress(label, address));
            }

            System.out.print("Add another optional field (yes/no): ");
            if (readYesNo(scanner.nextLine())) {
                System.out.print("Field name: ");
                String key = scanner.nextLine();
                System.out.print("Field value: ");
                String value = scanner.nextLine();
                updated.addOptionalField(key, value);
            }

            if (user.getContactBook().replaceContact(updated)) {
                System.out.println("Contact updated.");
            } else {
                System.out.println("Contact update failed.");
            }
        } catch (InvalidInputException e) {
            System.out.println("Edit failed: " + e.getMessage());
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
        String normalized = input.toUpperCase();
        switch (normalized) {
            case "FREE":
                return UserType.FREE;
            case "PREMIUM":
                return UserType.PREMIUM;
            default:
                throw new InvalidInputException("User Type is either FREE or PREMIUM");
        }
    }

    private static Optional<Contact> findContactById(List<Contact> contacts, String idInput) {
        try {
            UUID id = UUID.fromString(idInput);
            for (Contact contact : contacts) {
                if (contact.getId().equals(id)) {
                    return Optional.of(contact);
                }
            }
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}