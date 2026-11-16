package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save user with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update user with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find user with ID: '%s'.";
  private static final String MESSAGE_EMAIL = "Failed to find user with email: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all users.";
  private static final String MESSAGE_DELETE = "Failed to delete user with ID: '%s'.";
  private static final String MESSAGE_CONNECTION = "Could not establish database connection.";
  private static final String MESSAGE_STUDENT_SAVE = "Failed to save student with ID: '%s'.";
  private static final String MESSAGE_STUDENT_UPDATE = "Failed to update student with ID: '%s'.";
  private static final String MESSAGE_STUDENT_FIND = "Failed to find student with ID: '%s'.";
  private static final String MESSAGE_STUDENT_EMAIL = "Failed to find student with email: '%s'.";
  private static final String MESSAGE_STUDENT_ALL = "Failed to retrieve all students.";
  private static final String MESSAGE_STUDENT_DELETE = "Failed to delete student with ID: '%s'.";

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
          final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
          final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
          final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMAIL, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL, cause);
  }

  public static PersistenceException becauseDeleteFailed(
          final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE, userId), cause);
  }

  public static PersistenceException becauseConnectionFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_CONNECTION, cause);
  }


  //student


  public static PersistenceException becauseStudentSaveFailed(
          final String studentId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_STUDENT_SAVE, studentId), cause);
  }

  public static PersistenceException becauseStudentUpdateFailed(
          final String studentId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_STUDENT_UPDATE, studentId), cause);
  }

  public static PersistenceException becauseStudentFindByIdFailed(
          final String studentId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_STUDENT_FIND, studentId), cause);
  }

  public static PersistenceException becauseStudentFindByEmailFailed(
          final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_STUDENT_EMAIL, email), cause);
  }

  public static PersistenceException becauseStudentFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_STUDENT_ALL, cause);
  }

  public static PersistenceException becauseStudentDeleteFailed(
          final String studentId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_STUDENT_DELETE, studentId), cause);
  }
}
