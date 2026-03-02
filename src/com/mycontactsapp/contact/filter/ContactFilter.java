package com.mycontactsapp.contact.filter;

import com.mycontactsapp.contact.Contact;

/**
 * UC 10: Basic Filtering
 * Simple contract for different filter types.
 */
public interface ContactFilter {
    boolean matches(Contact contact, String term);
}
