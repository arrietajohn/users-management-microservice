package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.StudentId;

public interface DeleteStudentPort {
    void delete(StudentId studentId);
}
