package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentEmailException extends DomainException {

    private static final String MESSAGE_EMPTY = "The student email must not be empty.";
    private static final String MESSAGE_INVALID_FORMAT = "The student email format is invalid: '%s'.";

    private InvalidStudentEmailException(final String message) {
        super(message);
    }

    public static InvalidStudentEmailException becauseValueIsEmpty() {
        return new InvalidStudentEmailException(MESSAGE_EMPTY);
    }

    public static InvalidStudentEmailException becauseFormatIsInvalid(final String email) {
        return new InvalidStudentEmailException(String.format(MESSAGE_INVALID_FORMAT, email));
    }
}