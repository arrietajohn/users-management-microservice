package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import java.util.Optional;

public interface GetStudentByIdPort {
    Optional<StudentModel> getById(StudentId studentId);
}
