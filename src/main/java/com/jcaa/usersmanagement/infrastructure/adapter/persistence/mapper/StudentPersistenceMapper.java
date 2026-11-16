package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.StudentGender;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentBirthDate;
import com.jcaa.usersmanagement.domain.valueobject.StudentEmail;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentLastName;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import com.jcaa.usersmanagement.domain.valueobject.StudentPhone;
import com.jcaa.usersmanagement.domain.valueobject.StudentProgram;
import com.jcaa.usersmanagement.domain.valueobject.StudentSemester;
import com.jcaa.usersmanagement.domain.valueobject.StudentUniversity;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.StudentPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.StudentEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StudentPersistenceMapper {

    public StudentPersistenceDto fromModelToDto(final StudentModel student) {
        return new StudentPersistenceDto(
                student.getId().value(),
                student.getName().value(),
                student.getLastName().value(),
                student.getBirthDate().value().toString(),
                student.getSemester().value(),
                student.getEmail().value(),
                student.getGender().name(),
                student.getPhone().value(),
                student.getProgram().value(),
                student.getUniversity().value(),
                null,
                null);
    }

    public StudentEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
        return new StudentEntity(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("last_name"),
                resultSet.getString("birth_date"),
                resultSet.getInt("semester"),
                resultSet.getString("email"),
                resultSet.getString("gender"),
                resultSet.getString("phone"),
                resultSet.getString("program"),
                resultSet.getString("university"),
                resultSet.getString("created_at"),
                resultSet.getString("updated_at"));
    }

    public StudentModel fromEntityToModel(final StudentEntity entity) {
        return StudentModel.create(
                new StudentId(entity.id()),
                new StudentName(entity.name()),
                new StudentLastName(entity.lastName()),
                new StudentBirthDate(LocalDate.parse(entity.birthDate())),
                new StudentSemester(entity.semester()),
                new StudentEmail(entity.email()),
                StudentGender.fromString(entity.gender()),
                new StudentPhone(entity.phone()),
                new StudentProgram(entity.program()),
                new StudentUniversity(entity.university()));
    }

    public StudentModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
        return fromEntityToModel(fromResultSetToEntity(resultSet));
    }

    public List<StudentModel> fromResultSetToModelList(final ResultSet resultSet)
            throws SQLException {
        final List<StudentModel> students = new ArrayList<>();
        while (resultSet.next()) {
            students.add(fromResultSetToModel(resultSet));
        }
        return students;
    }
}
