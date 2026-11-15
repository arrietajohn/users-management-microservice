package com.jcaa.usersmanagement.domain.exception;

public final class InvalidStudentAcademicDataException extends DomainException {

    private static final String MESSAGE_EMPTY = "The student %s must not be empty.";

    private InvalidStudentAcademicDataException(final String message) {
        super(message);
    }

    public static InvalidStudentAcademicDataException becauseValueIsEmpty(final String fieldLabel) {
        return new InvalidStudentAcademicDataException(String.format(MESSAGE_EMPTY, fieldLabel));
    }
}