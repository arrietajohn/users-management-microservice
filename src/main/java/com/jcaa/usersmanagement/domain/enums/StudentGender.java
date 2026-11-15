package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidStudentGenderException;

public enum StudentGender {
    MALE,
    FEMALE,
    OTHER,
    UNSPECIFIED;

    public static StudentGender fromString(final String value) {
        for (final StudentGender gender : values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw InvalidStudentGenderException.becauseValueIsInvalid(value);
    }
}
