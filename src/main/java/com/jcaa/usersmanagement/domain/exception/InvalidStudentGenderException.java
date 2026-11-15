package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentGenderException extends DomainException {

    private static final String MESSAGE_INVALID = "The student gender '%s' is not valid.";

    private InvalidStudentGenderException(final String message) {
        super(message);
    }

    public static InvalidStudentGenderException becauseValueIsInvalid(final String gender) {
        return new InvalidStudentGenderException(String.format(MESSAGE_INVALID, gender));
    }
}