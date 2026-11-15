package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentAcademicDataException;
import java.util.Objects;

public record StudentUniversity(String value) {

    private static final String FIELD_LABEL = "university";

    public StudentUniversity {
        final String normalizedValue =
                Objects.requireNonNull(value, "StudentUniversity cannot be null").trim();
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidStudentAcademicDataException.becauseValueIsEmpty(FIELD_LABEL);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
