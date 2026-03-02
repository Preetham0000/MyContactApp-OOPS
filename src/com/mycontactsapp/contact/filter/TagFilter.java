package com.mycontactsapp.contact.filter;

import com.mycontactsapp.contact.Contact;
import com.mycontactsapp.contact.Tag;

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
        for (Tag tag : contact.getTags()) {
            if (tag.getName().equalsIgnoreCase(term)) {
                return true;
            }
        }
        return false;
    }
}