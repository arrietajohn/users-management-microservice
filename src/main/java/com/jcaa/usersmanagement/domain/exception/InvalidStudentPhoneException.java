package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentPhoneException extends DomainException {

    private static final String MESSAGE_EMPTY = "The student phone must not be empty.";
    private static final String MESSAGE_INVALID_FORMAT =
            "The student phone format is invalid: '%s'. It must have between 7 and 15 digits.";

    private InvalidStudentPhoneException(final String message) {
        super(message);
    }

    public static InvalidStudentPhoneException becauseValueIsEmpty() {
        return new InvalidStudentPhoneException(MESSAGE_EMPTY);
    }

    public static InvalidStudentPhoneException becauseFormatIsInvalid(final String phone) {
        return new InvalidStudentPhoneException(String.format(MESSAGE_INVALID_FORMAT, phone));
    }
}
