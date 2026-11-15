package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentNameException;
import java.util.Objects;
import java.util.regex.Pattern;

public record StudentLastName(String value) {

    private static final String FIELD_LABEL = "last name";
    private static final int MINIMUM_LENGTH = 2;
    private static final Pattern LETTERS_PATTERN = Pattern.compile("^\\p{L}[\\p{L} '.\\-]*$");

    public StudentLastName {
        final String normalizedValue =
                Objects.requireNonNull(value, "StudentLastName cannot be null").trim();
        validateNotEmpty(normalizedValue);
        validateMinimumLength(normalizedValue);
        validateCharacters(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidStudentNameException.becauseValueIsEmpty(FIELD_LABEL);
        }
    }

    private static void validateMinimumLength(final String normalizedValue) {
        if (normalizedValue.length() < MINIMUM_LENGTH) {
            throw InvalidStudentNameException.becauseLengthIsTooShort(FIELD_LABEL, MINIMUM_LENGTH);
        }
    }

    private static void validateCharacters(final String normalizedValue) {
        if (!LETTERS_PATTERN.matcher(normalizedValue).matches()) {
            throw InvalidStudentNameException.becauseCharactersAreInvalid(FIELD_LABEL, normalizedValue);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
