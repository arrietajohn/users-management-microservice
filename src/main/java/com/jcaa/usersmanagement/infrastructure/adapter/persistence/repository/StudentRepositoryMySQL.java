package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteStudentPort;
import com.jcaa.usersmanagement.application.port.out.GetAllStudentsPort;
import com.jcaa.usersmanagement.application.port.out.GetStudentByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveStudentPort;
import com.jcaa.usersmanagement.application.port.out.UpdateStudentPort;
import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentEmail;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.StudentPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.StudentPersistenceMapper;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StudentRepositoryMySQL
        implements SaveStudentPort,
        UpdateStudentPort,
        GetStudentByIdPort,
        GetStudentByEmailPort,
        GetAllStudentsPort,
        DeleteStudentPort {

    private static final String COLUMNS =
            "id, name, last_name, birth_date, semester, email, gender, phone, program, university, "
                    + "created_at, updated_at ";

    private static final String SQL_INSERT =
            "INSERT INTO students "
                    + "(id, name, last_name, birth_date, semester, email, gender, phone, program, "
                    + "university, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SQL_UPDATE =
            "UPDATE students SET name = ?, last_name = ?, birth_date = ?, semester = ?, email = ?, "
                    + "gender = ?, phone = ?, program = ?, university = ?, updated_at = NOW() "
                    + "WHERE id = ?";

    private static final String SQL_SELECT_BY_ID =
            "SELECT " + COLUMNS + "FROM students WHERE id = ? LIMIT 1";

    private static final String SQL_SELECT_BY_EMAIL =
            "SELECT " + COLUMNS + "FROM students WHERE email = ? LIMIT 1";

    private static final String SQL_SELECT_ALL =
            "SELECT " + COLUMNS + "FROM students ORDER BY last_name ASC, name ASC";

    private static final String SQL_DELETE = "DELETE FROM students WHERE id = ?";

    private final DataSource dataSource;

    @Override
    public StudentModel save(final StudentModel student) {
        final StudentPersistenceDto dto = StudentPersistenceMapper.fromModelToDto(student);
        executeSave(dto);
        return findByIdOrFail(student.getId());
    }

    @Override
    public StudentModel update(final StudentModel student) {
        final StudentPersistenceDto dto = StudentPersistenceMapper.fromModelToDto(student);
        executeUpdate(dto);
        return findByIdOrFail(student.getId());
    }

    @Override
    public Optional<StudentModel> getById(final StudentId studentId) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setString(1, studentId.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(StudentPersistenceMapper.fromResultSetToModel(resultSet));
        } catch (final SQLException exception) {
            throw PersistenceException.becauseStudentFindByIdFailed(studentId.value(), exception);
        }
    }

    @Override
    public Optional<StudentModel> getByEmail(final StudentEmail email) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
            statement.setString(1, email.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(StudentPersistenceMapper.fromResultSetToModel(resultSet));
        } catch (final SQLException exception) {
            throw PersistenceException.becauseStudentFindByEmailFailed(email.value(), exception);
        }
    }

    @Override
    public List<StudentModel> getAll() {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            final ResultSet resultSet = statement.executeQuery();
            return StudentPersistenceMapper.fromResultSetToModelList(resultSet);
        } catch (final SQLException exception) {
            throw PersistenceException.becauseStudentFindAllFailed(exception);
        }
    }

    @Override
    public void delete(final StudentId studentId) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setString(1, studentId.value());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseStudentDeleteFailed(studentId.value(), exception);
        }
    }

    private void executeSave(final StudentPersistenceDto dto) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, dto.id());
            statement.setString(2, dto.name());
            statement.setString(3, dto.lastName());
            statement.setDate(4, Date.valueOf(LocalDate.parse(dto.birthDate())));
            statement.setInt(5, dto.semester());
            statement.setString(6, dto.email());
            statement.setString(7, dto.gender());
            statement.setString(8, dto.phone());
            statement.setString(9, dto.program());
            statement.setString(10, dto.university());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseStudentSaveFailed(dto.id(), exception);
        }
    }

    private void executeUpdate(final StudentPersistenceDto dto) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, dto.name());
            statement.setString(2, dto.lastName());
            statement.setDate(3, Date.valueOf(LocalDate.parse(dto.birthDate())));
            statement.setInt(4, dto.semester());
            statement.setString(5, dto.email());
            statement.setString(6, dto.gender());
            statement.setString(7, dto.phone());
            statement.setString(8, dto.program());
            statement.setString(9, dto.university());
            statement.setString(10, dto.id());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseStudentUpdateFailed(dto.id(), exception);
        }
    }

    private StudentModel findByIdOrFail(final StudentId studentId) {
        return getById(studentId)
                .orElseThrow(() -> StudentNotFoundException.becauseIdWasNotFound(studentId.value()));
    }
}
