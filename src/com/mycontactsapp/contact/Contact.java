package com.mycontactsapp.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mycontactsapp.ExceptionHandling.InvalidInputException;
import com.mycontactsapp.validation.EmailValidator;

/**
 * Base type for contacts with phones, emails, and optional fields.
 */
public abstract class Contact {
    // Base contact holds shared details used by both people and organizations.
    // It also collects phones/emails as separate value objects.
    private final UUID id;
    private String displayName;
    private final List<PhoneNumber> phoneNumbers;
    private final List<EmailAddress> emailAddresses;
    private final Map<String, String> optionalFields;
    private final List<String> tags;

    protected Contact(String displayName) throws InvalidInputException {
        if (displayName == null || displayName.isEmpty()) {
            throw new InvalidInputException("Display name is required.");
        }
        this.id = UUID.randomUUID();
        this.displayName = displayName;
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        this.optionalFields = new LinkedHashMap<>();
        this.tags = new ArrayList<>();
    }

    protected Contact(Contact other) throws InvalidInputException {
        if (other == null) {
            throw new InvalidInputException("Contact to copy is required.");
        }
        this.id = other.id;
        this.displayName = other.displayName;
        this.phoneNumbers = new ArrayList<>(other.phoneNumbers);
        this.emailAddresses = new ArrayList<>(other.emailAddresses);
        this.optionalFields = new LinkedHashMap<>(other.optionalFields);
        this.tags = new ArrayList<>(other.tags);
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    protected void setDisplayName(String displayName) throws InvalidInputException {
        if (displayName == null || displayName.isEmpty()) {
            throw new InvalidInputException("Display name is required.");
        }
        this.displayName = displayName;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return Collections.unmodifiableList(phoneNumbers);
    }

    public List<EmailAddress> getEmailAddresses() {
        return Collections.unmodifiableList(emailAddresses);
    }

    public Map<String, String> getOptionalFields() {
        return Collections.unmodifiableMap(optionalFields);
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) throws InvalidInputException {
        if (phoneNumber == null) {
            throw new InvalidInputException("Phone number is required.");
        }
        phoneNumbers.add(phoneNumber);
    }

    public void addEmailAddress(EmailAddress emailAddress) throws InvalidInputException {
        if (emailAddress == null) {
            throw new InvalidInputException("Email address is required.");
        }
        emailAddresses.add(emailAddress);
    }

    public void addOptionalField(String key, String value) throws InvalidInputException {
        if (key == null || key.isEmpty()) {
            throw new InvalidInputException("Field name is required.");
        }
        if (value == null || value.isEmpty()) {
            throw new InvalidInputException("Field value is required.");
        }
        optionalFields.put(key, value);
    }

    public void addTag(String tag) throws InvalidInputException {
        if (tag == null || tag.isEmpty()) {
            throw new InvalidInputException("Tag is required.");
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public ContactView toView() {
        String contactType = getClass().getSimpleName();
        Optional<String> orgName = Optional.empty();
        Optional<String> contactPerson = Optional.empty();
        Optional<String> personName = Optional.empty();

        if (this instanceof OrganizationContact) {
            OrganizationContact org = (OrganizationContact) this;
            orgName = Optional.of(org.getOrganizationName());
            if (org.getContactPerson() != null && !org.getContactPerson().isEmpty()) {
                contactPerson = Optional.of(org.getContactPerson());
            }
        }

        if (this instanceof PersonContact) {
            PersonContact person = (PersonContact) this;
            personName = Optional.of(person.getFirstName() + " " + person.getLastName());
        }

        return new ContactView(id, displayName, contactType, phoneNumbers, emailAddresses, optionalFields, tags,
                personName, orgName, contactPerson);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                ", emailAddresses=" + emailAddresses +
                ", optionalFields=" + optionalFields +
                ", tags=" + tags +
                '}';
    }

    public static class PhoneNumber {
        // Phone number is stored as its own object, attached to a contact.
        // This keeps label and number together in one place.
        private final String label;
        private final String number;

        public PhoneNumber(String label, String number) throws InvalidInputException {
            if (label == null || label.isEmpty()) {
                throw new InvalidInputException("Phone label is required.");
            }
            if (number == null || number.isEmpty()) {
                throw new InvalidInputException("Phone number is required.");
            }
            this.label = label;
            this.number = number;
        }

        public String getLabel() {
            return label;
        }

        public String getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return label + ": " + number;
        }
    }

    public static class EmailAddress {
        // Email address is stored as its own object, attached to a contact.
        // This keeps label and address together in one place.
        private final String label;
        private final String address;

        public EmailAddress(String label, String address) throws InvalidInputException {
            if (label == null || label.isEmpty()) {
                throw new InvalidInputException("Email label is required.");
            }
            EmailValidator validator = new EmailValidator();
            this.label = label;
            this.address = validator.validate(address);
        }

        public String getLabel() {
            return label;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return label + ": " + address;
        }
    }

    public static class PersonContact extends Contact {
        // Person contact extends the base contact with first and last names.
        // This shows the contact hierarchy for individuals.
        private String firstName;
        private String lastName;

        public PersonContact(String firstName, String lastName) throws InvalidInputException {
            super(buildDisplayName(firstName, lastName));
            if (firstName == null || firstName.isEmpty()) {
                throw new InvalidInputException("First name is required.");
            }
            if (lastName == null || lastName.isEmpty()) {
                throw new InvalidInputException("Last name is required.");
            }
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public PersonContact(PersonContact other) throws InvalidInputException {
            super(other);
            this.firstName = other.firstName;
            this.lastName = other.lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setFirstName(String firstName) throws InvalidInputException {
            if (firstName == null || firstName.isEmpty()) {
                throw new InvalidInputException("First name is required.");
            }
            this.firstName = firstName;
            setDisplayName(buildDisplayName(this.firstName, this.lastName));
        }

        public void setLastName(String lastName) throws InvalidInputException {
            if (lastName == null || lastName.isEmpty()) {
                throw new InvalidInputException("Last name is required.");
            }
            this.lastName = lastName;
            setDisplayName(buildDisplayName(this.firstName, this.lastName));
        }

        private static String buildDisplayName(String firstName, String lastName) throws InvalidInputException {
            if (firstName == null || firstName.isEmpty()) {
                throw new InvalidInputException("First name is required.");
            }
            if (lastName == null || lastName.isEmpty()) {
                throw new InvalidInputException("Last name is required.");
            }
            return firstName + " " + lastName;
        }
    }

    public static class OrganizationContact extends Contact {
        // Organization contact extends the base contact with organization details.
        // This shows the contact hierarchy for businesses.
        private String organizationName;
        private String contactPerson;

        public OrganizationContact(String organizationName, String contactPerson) throws InvalidInputException {
            super(validateOrgName(organizationName));
            this.organizationName = validateOrgName(organizationName);
            this.contactPerson = contactPerson == null ? "" : contactPerson;
        }

        public OrganizationContact(OrganizationContact other) throws InvalidInputException {
            super(other);
            this.organizationName = other.organizationName;
            this.contactPerson = other.contactPerson;
        }

        public String getOrganizationName() {
            return organizationName;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setOrganizationName(String organizationName) throws InvalidInputException {
            this.organizationName = validateOrgName(organizationName);
            setDisplayName(this.organizationName);
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson == null ? "" : contactPerson;
        }

        private static String validateOrgName(String organizationName) throws InvalidInputException {
            if (organizationName == null || organizationName.isEmpty()) {
                throw new InvalidInputException("Organization name is required.");
            }
            return organizationName;
        }
    }

    public static class ContactBook {
        // Contact book is a simple list owned by a user.
        // It groups many contacts together in one place.
        private final List<Contact> contacts = new ArrayList<>();

        public void addContact(Contact contact) throws InvalidInputException {
            if (contact == null) {
                throw new InvalidInputException("Contact is required.");
            }
            contacts.add(contact);
        }

        public boolean replaceContact(Contact updated) throws InvalidInputException {
            if (updated == null) {
                throw new InvalidInputException("Updated contact is required.");
            }
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getId().equals(updated.getId())) {
                    contacts.set(i, updated);
                    return true;
                }
            }
            return false;
        }

        public Optional<Contact> findById(String idInput) {
            try {
                UUID id = UUID.fromString(idInput);
                for (Contact contact : contacts) {
                    if (contact.getId().equals(id)) {
                        return Optional.of(contact);
                    }
                }
                return Optional.empty();
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }

        public boolean removeById(String idInput) {
            Optional<Contact> found = findById(idInput);
            if (!found.isPresent()) {
                return false;
            }
            return contacts.remove(found.get());
        }

        public int bulkRemove(List<String> ids) {
            int removed = 0;
            if (ids == null) {
                return 0;
            }
            for (String id : ids) {
                if (id != null && removeById(id)) {
                    removed++;
                }
            }
            return removed;
        }

        public int bulkAddTag(List<String> ids, String tag) throws InvalidInputException {
            int updated = 0;
            if (ids == null) {
                return 0;
            }
            for (String id : ids) {
                Optional<Contact> found = findById(id);
                if (found.isPresent()) {
                    found.get().addTag(tag);
                    updated++;
                }
            }
            return updated;
        }

        public String exportCsv(List<Contact> selection) {
            StringBuilder builder = new StringBuilder();
            builder.append("id,displayName,type,tags,phones,emails\n");
            if (selection == null) {
                return builder.toString();
            }
            for (Contact contact : selection) {
                builder.append(contact.getId()).append(",")
                        .append(contact.getDisplayName()).append(",")
                        .append(contact.getClass().getSimpleName()).append(",")
                        .append(contact.getTags()).append(",")
                        .append(contact.getPhoneNumbers()).append(",")
                        .append(contact.getEmailAddresses()).append("\n");
            }
            return builder.toString();
        }

        public List<Contact> getContacts() {
            return Collections.unmodifiableList(contacts);
        }
    }

    public static class ContactView {
        private final UUID id;
        private final String displayName;
        private final String contactType;
        private final List<PhoneNumber> phoneNumbers;
        private final List<EmailAddress> emailAddresses;
        private final Map<String, String> optionalFields;
        private final List<String> tags;
        private final Optional<String> personName;
        private final Optional<String> organizationName;
        private final Optional<String> contactPerson;

        public ContactView(UUID id, String displayName, String contactType,
                List<PhoneNumber> phoneNumbers, List<EmailAddress> emailAddresses,
                Map<String, String> optionalFields, Optional<String> personName,
                Optional<String> organizationName, Optional<String> contactPerson) {
            this.id = id;
            this.displayName = displayName;
            this.contactType = contactType;
            this.phoneNumbers = Collections.unmodifiableList(new ArrayList<>(phoneNumbers));
            this.emailAddresses = Collections.unmodifiableList(new ArrayList<>(emailAddresses));
            this.optionalFields = Collections.unmodifiableMap(new LinkedHashMap<>(optionalFields));
            this.tags = Collections.emptyList();
            this.personName = personName;
            this.organizationName = organizationName;
            this.contactPerson = contactPerson;
        }

        public ContactView(UUID id, String displayName, String contactType,
                List<PhoneNumber> phoneNumbers, List<EmailAddress> emailAddresses,
                Map<String, String> optionalFields, List<String> tags,
                Optional<String> personName, Optional<String> organizationName,
                Optional<String> contactPerson) {
            this.id = id;
            this.displayName = displayName;
            this.contactType = contactType;
            this.phoneNumbers = Collections.unmodifiableList(new ArrayList<>(phoneNumbers));
            this.emailAddresses = Collections.unmodifiableList(new ArrayList<>(emailAddresses));
            this.optionalFields = Collections.unmodifiableMap(new LinkedHashMap<>(optionalFields));
            this.tags = Collections.unmodifiableList(new ArrayList<>(tags));
            this.personName = personName;
            this.organizationName = organizationName;
            this.contactPerson = contactPerson;
        }

        @Override
        public String toString() {
            return String.format("Contact Details\nID: %s\nName: %s\nType: %s\nTags: %s\nPerson: %s\nOrganization: %s\nContact Person: %s\nPhones: %s\nEmails: %s\nOptional: %s",
                    id,
                    displayName,
                    contactType,
                    tags,
                    personName.orElse("N/A"),
                    organizationName.orElse("N/A"),
                    contactPerson.orElse("N/A"),
                    phoneNumbers,
                    emailAddresses,
                    optionalFields);
        }
    }
}
