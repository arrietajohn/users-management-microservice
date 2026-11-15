package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentNameException extends DomainException {

    private static final String MESSAGE_EMPTY = "The student %s must not be empty.";
    private static final String MESSAGE_TOO_SHORT =
            "The student %s must have at least %d characters.";
    private static final String MESSAGE_INVALID_CHARACTERS =
            "The student %s contains invalid characters: '%s'.";

    private InvalidStudentNameException(final String message) {
        super(message);
    }

    public static InvalidStudentNameException becauseValueIsEmpty(final String fieldLabel) {
        return new InvalidStudentNameException(String.format(MESSAGE_EMPTY, fieldLabel));
    }

    public static InvalidStudentNameException becauseLengthIsTooShort(
            final String fieldLabel, final int minimumLength) {
        return new InvalidStudentNameException(
                String.format(MESSAGE_TOO_SHORT, fieldLabel, minimumLength));
    }

    public static InvalidStudentNameException becauseCharactersAreInvalid(
            final String fieldLabel, final String value) {
        return new InvalidStudentNameException(
                String.format(MESSAGE_INVALID_CHARACTERS, fieldLabel, value));
    }
}
