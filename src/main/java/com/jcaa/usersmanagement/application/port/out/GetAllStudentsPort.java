package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.StudentModel;
import java.util.List;

public interface GetAllStudentsPort {
    List<StudentModel> getAll();
}
