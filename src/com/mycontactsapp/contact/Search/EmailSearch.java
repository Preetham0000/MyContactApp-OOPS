package com.mycontactsapp.contact.Search;

import com.mycontactsapp.contact.Contact;

/**
 * UC 9: Search Contacts
 * Email search checks each email address for matches.
 */
public class EmailSearch implements ContactSearch {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null || term == null || term.isEmpty()) {
            return false;
        }
        for (Contact.EmailAddress email : contact.getEmailAddresses()) {
            if (containsIgnoreCase(email.getAddress(), term)) {
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
