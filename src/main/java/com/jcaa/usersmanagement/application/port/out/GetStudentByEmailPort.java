package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentEmail;
import java.util.Optional;

public interface GetStudentByEmailPort {
    Optional<StudentModel> getByEmail(StudentEmail email);
}
