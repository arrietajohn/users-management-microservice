package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentSemesterException;

public record StudentSemester(int value) {

    private static final int MINIMUM = 1;
    private static final int MAXIMUM = 12;

    public StudentSemester {
        validateRange(value);
    }

    private static void validateRange(final int value) {
        if (value < MINIMUM || value > MAXIMUM) {
            throw InvalidStudentSemesterException.becauseValueIsOutOfRange(MINIMUM, MAXIMUM, value);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
