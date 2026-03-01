package com.mycontactsapp.Storage;

import java.util.HashMap;
import java.util.Map;

public class UserStore {
    // email -> password
    private static final Map<String, String> USERS = new HashMap<>();

    private UserStore() {
    }

    public static void saveUser(String email, String password) {
        USERS.put(email, password);
    }

    public static boolean isValidUser(String email, String password) {
        return USERS.containsKey(email) &&
               USERS.get(email).equals(password);
    }
}
