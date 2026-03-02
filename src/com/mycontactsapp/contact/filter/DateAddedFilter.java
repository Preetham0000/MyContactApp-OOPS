package com.mycontactsapp.contact.filter;

import com.mycontactsapp.contact.Contact;

/**
 * UC 10: Basic Filtering
 * Date filter checks if the date added matches.
 */
public class DateAddedFilter implements ContactFilter {
    @Override
    public boolean matches(Contact contact, String term) {
        if (contact == null || term == null || term.isEmpty()) {
            return false;
        }
        String addedDate = contact.getDateAdded().toLocalDate().toString();
        return addedDate.equals(term);
    }
}
