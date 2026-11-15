package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.StudentModel;

public interface UpdateStudentPort {
    StudentModel update(StudentModel student);
}
