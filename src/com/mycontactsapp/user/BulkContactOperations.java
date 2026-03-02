package com.mycontactsapp.user;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.contact.Contact;

/**
 * UC 8: Bulk Operations
 * Simple helper for delete, tag, and export across many contacts.
 */
public class BulkContactOperations {
    public int bulkDelete(Contact.ContactBook book, List<String> ids) throws InvalidInputException {
        if (book == null) {
            throw new InvalidInputException("Contact book is required.");
        }
        return book.bulkRemove(deduplicate(ids));
    }

    public int bulkTag(Contact.ContactBook book, List<String> ids, String tag) throws InvalidInputException {
        if (book == null) {
            throw new InvalidInputException("Contact book is required.");
        }
        if (tag == null || tag.isEmpty()) {
            throw new InvalidInputException("Tag is required.");
        }
        return book.bulkAddTag(deduplicate(ids), tag);
    }

    public String exportToFile(Contact.ContactBook book, List<String> ids, String filePath)
            throws InvalidInputException {
        if (book == null) {
            throw new InvalidInputException("Contact book is required.");
        }
        if (filePath == null || filePath.isEmpty()) {
            throw new InvalidInputException("File path is required.");
        }
        List<Contact> selection = selectContacts(book, ids);
        String csv = book.exportCsv(selection);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(csv);
        } catch (IOException e) {
            throw new InvalidInputException("Export failed: " + e.getMessage());
        }
        return filePath;
    }

    private List<String> deduplicate(List<String> ids) {
        if (ids == null) {
            return new ArrayList<>();
        }
        Set<String> unique = new LinkedHashSet<>(ids);
        return new ArrayList<>(unique);
    }

    private List<Contact> selectContacts(Contact.ContactBook book, List<String> ids) {
        List<Contact> selection = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            selection.addAll(book.getContacts());
            return selection;
        }
        for (String id : deduplicate(ids)) {
            if (id == null || id.isEmpty()) {
                continue;
            }
            book.findById(id).ifPresent(selection::add);
        }
        return selection;
    }
}
