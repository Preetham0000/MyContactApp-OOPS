package com.mycontactsapp.contact.filter;

import com.mycontactsapp.contact.Contact;

/**
 * UC 10: Basic Filtering
 * Tag filter checks if a tag is present.
 */
public class TagFilter implements ContactFilter {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null || term == null || term.isEmpty()) {
            return false;
        }
        for (String tag : contact.getTags()) {
            if (tag != null && tag.equalsIgnoreCase(term)) {
                return true;
            }
        }
        return false;
    }
}
