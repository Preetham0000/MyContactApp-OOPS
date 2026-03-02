package com.mycontactsapp.contact.Search;

import com.mycontactsapp.contact.Contact;

/**
 * UC 9: Search Contacts
 * Tag search checks tags for matches.
 */
public class TagSearch implements ContactSearch {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null || term == null || term.isEmpty()) {
            return false;
        }
        for (String tag : contact.getTags()) {
            if (containsIgnoreCase(tag, term)) {
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
