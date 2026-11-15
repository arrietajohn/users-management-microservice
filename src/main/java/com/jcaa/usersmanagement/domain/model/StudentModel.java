package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.StudentGender;
import com.jcaa.usersmanagement.domain.valueobject.StudentBirthDate;
import com.jcaa.usersmanagement.domain.valueobject.StudentEmail;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentLastName;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import com.jcaa.usersmanagement.domain.valueobject.StudentPhone;
import com.jcaa.usersmanagement.domain.valueobject.StudentProgram;
import com.jcaa.usersmanagement.domain.valueobject.StudentSemester;
import com.jcaa.usersmanagement.domain.valueobject.StudentUniversity;
import lombok.Value;

@Value
public class StudentModel {

    StudentId id;
    StudentName name;
    StudentLastName lastName;
    StudentBirthDate birthDate;
    StudentSemester semester;
    StudentEmail email;
    StudentGender gender;
    StudentPhone phone;
    StudentProgram program;
    StudentUniversity university;

    public static StudentModel create(
            final StudentId id,
            final StudentName name,
            final StudentLastName lastName,
            final StudentBirthDate birthDate,
            final StudentSemester semester,
            final StudentEmail email,
            final StudentGender gender,
            final StudentPhone phone,
            final StudentProgram program,
            final StudentUniversity university) {
        return new StudentModel(
                id, name, lastName, birthDate, semester, email, gender, phone, program, university);
    }

    public StudentModel update(
            final StudentName newName,
            final StudentLastName newLastName,
            final StudentBirthDate newBirthDate,
            final StudentSemester newSemester,
            final StudentEmail newEmail,
            final StudentGender newGender,
            final StudentPhone newPhone,
            final StudentProgram newProgram,
            final StudentUniversity newUniversity) {
        return new StudentModel(
                id,
                newName,
                newLastName,
                newBirthDate,
                newSemester,
                newEmail,
                newGender,
                newPhone,
                newProgram,
                newUniversity);
    }
}
