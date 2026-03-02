package com.mycontactsapp.contact.Search;

import com.mycontactsapp.contact.Contact;

/**
 * UC 9: Search Contacts
 * Phone search checks each phone number for matches.
 */
public class PhoneSearch implements ContactSearch {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null || term == null || term.isEmpty()) {
            return false;
        }
        for (Contact.PhoneNumber phone : contact.getPhoneNumbers()) {
            if (containsIgnoreCase(phone.getNumber(), term)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsIgnoreCase(String value, String term) {
        if (value == null) {
            return false;
        }
        return value.toLowerCase().contains(term.toLowerCase());
    }
}
