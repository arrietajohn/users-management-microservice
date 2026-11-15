package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentEmailException;
import java.util.Objects;
import java.util.regex.Pattern;

public record StudentEmail(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    public StudentEmail {
        final String normalizedValue =
                Objects.requireNonNull(value, "StudentEmail cannot be null").trim().toLowerCase();
        validateNotEmpty(normalizedValue);
        validateFormat(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidStudentEmailException.becauseValueIsEmpty();
        }
    }

    private static void validateFormat(final String normalizedValue) {
        if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
            throw InvalidStudentEmailException.becauseFormatIsInvalid(normalizedValue);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
