package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentBirthDateException extends DomainException {

    private static final String MESSAGE_IN_THE_FUTURE =
            "The student birth date must not be in the future: '%s'.";
    private static final String MESSAGE_TOO_YOUNG = "The student must be at least %d years old.";

    private InvalidStudentBirthDateException(final String message) {
        super(message);
    }

    public static InvalidStudentBirthDateException becauseDateIsInTheFuture(final String date) {
        return new InvalidStudentBirthDateException(String.format(MESSAGE_IN_THE_FUTURE, date));
    }

    public static InvalidStudentBirthDateException becauseStudentIsTooYoung(final int minimumAge) {
        return new InvalidStudentBirthDateException(String.format(MESSAGE_TOO_YOUNG, minimumAge));
    }
}
