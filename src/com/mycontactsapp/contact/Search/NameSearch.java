package com.mycontactsapp.contact.Search;

import com.mycontactsapp.contact.Contact;

/**
 * UC 9: Search Contacts
 * Name search checks display name for matches.
 */
public class NameSearch implements ContactSearch {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null || term == null || term.isEmpty()) {
            return false;
        }
        return containsIgnoreCase(contact.getDisplayName(), term);
    }

    private boolean containsIgnoreCase(String value, String term) {
        if (value == null) {
            return false;
        }
        return value.toLowerCase().contains(term.toLowerCase());
    }
}
