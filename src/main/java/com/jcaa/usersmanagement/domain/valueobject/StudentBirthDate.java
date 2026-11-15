package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentBirthDateException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public record StudentBirthDate(LocalDate value) {

    private static final int MINIMUM_AGE = 14;

    public StudentBirthDate {
        Objects.requireNonNull(value, "StudentBirthDate cannot be null");
        validateNotInTheFuture(value);
        validateMinimumAge(value);
    }

    private static void validateNotInTheFuture(final LocalDate value) {
        if (value.isAfter(LocalDate.now())) {
            throw InvalidStudentBirthDateException.becauseDateIsInTheFuture(value.toString());
        }
    }

    private static void validateMinimumAge(final LocalDate value) {
        if (Period.between(value, LocalDate.now()).getYears() < MINIMUM_AGE) {
            throw InvalidStudentBirthDateException.becauseStudentIsTooYoung(MINIMUM_AGE);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
