package com.mycontactsapp.contact;

import java.util.Objects;

/**
 * UC 11: Create and Manage Tags
 * Simple tag value object.
 */
public class Tag {
    private final String name;

    public Tag(String name) {
        this.name = name == null ? "" : name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Tag)) {
            return false;
        }
        Tag that = (Tag) other;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
