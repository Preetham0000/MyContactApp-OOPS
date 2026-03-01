package com.mycontactsapp.profile;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.user.UserType;

/**
 * UC 3: User Profile Management
 * - Stores user preference settings
 * - Validates preference updates
 */
public class UserPreferences {
    private boolean emailNotifications;
    private UserType preferredUserType;

    public UserPreferences() {
        this.emailNotifications = true;
        this.preferredUserType = UserType.FREE;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public UserType getPreferredUserType() {
        return preferredUserType;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public void setPreferredUserType(UserType preferredUserType) throws InvalidInputException {
        if (preferredUserType == null) {
            throw new InvalidInputException("Preferred user type is required.");
        }
        this.preferredUserType = preferredUserType;
    }

    public UserType togglePreferredUserType() {
        this.preferredUserType = (this.preferredUserType == UserType.PREMIUM)
                ? UserType.FREE
                : UserType.PREMIUM;
        return this.preferredUserType;
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
                "emailNotifications=" + emailNotifications +
                ", preferredUserType=" + preferredUserType +
                '}';
    }
}