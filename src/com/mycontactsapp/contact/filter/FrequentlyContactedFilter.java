package com.mycontactsapp.contact.filter;

import com.mycontactsapp.contact.Contact;

/**
 * UC 10: Basic Filtering
 * Frequently contacted checks minimum view count.
 */
public class FrequentlyContactedFilter implements ContactFilter {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null) {
            return false;
        }
        int minCount = 1;
        if (term != null && !term.isEmpty()) {
            try {
                minCount = Integer.parseInt(term);
            } catch (NumberFormatException e) {
                minCount = 1;
            }
        }
        return contact.getContactCount() >= minCount;
    }
}
