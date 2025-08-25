package com.crediya.loans.domain.model;

/**
 * Represents the status of a loan application.
 */
public class Status {

    private final Long id;
    private final String name;
    private final String description;

    public Status(Long id, String name, String description) {

        validateName(name);
        validateDescription(description);

        this.id = id;
        this.name = name;
        this.description = description;
    }


    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Status name cannot be null or empty.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Status description cannot be null or empty.");
        }
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
