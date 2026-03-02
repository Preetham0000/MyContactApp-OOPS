package com.mycontactsapp.contact.Search;

import com.mycontactsapp.contact.Contact;

/**
 * UC 9: Search Contacts
 * Simple contract for different search types.
 */
public interface ContactSearch {
    boolean matches(Contact contact, String term);
}
