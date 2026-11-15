package com.jcaa.usersmanagement.domain.exception;

public class InvalidStudentEmailException extends RuntimeException {
  public InvalidStudentEmailException(String message) {
    super(message);
  }
}
