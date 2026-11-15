package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentPhoneException;
import java.util.Objects;
import java.util.regex.Pattern;

public record StudentPhone(String value) {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{7,15}$");

    public StudentPhone {
        final String normalizedValue =
                Objects.requireNonNull(value, "StudentPhone cannot be null").replaceAll("[\\s-]", "");
        validateNotEmpty(normalizedValue);
        validateFormat(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidStudentPhoneException.becauseValueIsEmpty();
        }
    }

    private static void validateFormat(final String normalizedValue) {
        if (!PHONE_PATTERN.matcher(normalizedValue).matches()) {
            throw InvalidStudentPhoneException.becauseFormatIsInvalid(normalizedValue);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
