package com.jcaa.usersmanagement.domain.exception;

public final class StudentAlreadyExistsException extends DomainException {

    private static final String MESSAGE_EMAIL_EXISTS = "A student with email '%s' already exists.";

    private StudentAlreadyExistsException(final String message) {
        super(message);
    }

    public static StudentAlreadyExistsException becauseEmailAlreadyExists(final String email) {
        return new StudentAlreadyExistsException(String.format(MESSAGE_EMAIL_EXISTS, email));
    }
}
