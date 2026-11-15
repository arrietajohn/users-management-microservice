package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentSemesterException extends DomainException {

    private static final String MESSAGE_OUT_OF_RANGE =
            "The student semester must be between %d and %d, but was %d.";

    private InvalidStudentSemesterException(final String message) {
        super(message);
    }

    public static InvalidStudentSemesterException becauseValueIsOutOfRange(
            final int minimum, final int maximum, final int value) {
        return new InvalidStudentSemesterException(
                String.format(MESSAGE_OUT_OF_RANGE, minimum, maximum, value));
    }
}
