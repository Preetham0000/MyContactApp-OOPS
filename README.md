# MyContactApp


## Use Cases

1. **UC-01: User Registration**
   - User creates an account with basic details.
   - Input is validated before saving.

2. **UC-02: User Authentication**
   - User logs in with email and password.
   - Credentials are checked before access is granted.

3. **UC-03: User Profile Management**
   - User updates profile info and password.
   - User manages simple preferences.

4. **UC-04: Create Contact**
   - User creates a person or organization contact.
   - Multiple phones/emails and optional fields are supported.

5. **UC-05: View Contact Details**
   - User views full details of a contact.
   - Data is formatted for display.

6. **UC-06: Edit Contact**
   - User edits an existing contact.
   - Validation runs before changes are saved.

7. **UC-07: Delete Contact**
   - User removes a contact with confirmation.
   - Related data is removed with the contact.

8. **UC-08: Bulk Operations**
   - User deletes, tags, or exports multiple contacts.
   - Operations use simple loops over collections.

9. **UC-09: Search Contacts**
   - User searches by name, phone, email, or tag.
   - Simple string checks find matches.

10. **UC-10: Basic Filtering**
    - User filters by tag, date added, or frequently contacted.
    - Results can be sorted for easy viewing.

11. **UC-11: Create and Manage Tags**
    - User creates unique tags for organizing contacts.
    - Tags are stored in a set to avoid duplicates.

12. **UC-12: Apply Tags to Contacts**
    - User adds or removes tags on a contact.
    - Tag changes keep contact relationships up to date.
