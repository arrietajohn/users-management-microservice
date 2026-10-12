package com.jcaa.usersmanagement.infrastructure.config;

public final class ConfigurationException extends RuntimeException {

  private static final String MESSAGE_LOAD = "Failed to load the application configuration.";
  private static final String MESSAGE_JWT_SECRET_TOO_SHORT =
      "jwt.secret must have at least %d bytes for HMAC-SHA256.";
  private static final String MESSAGE_JWT_ISSUER_BLANK = "jwt.issuer must not be blank.";
  private static final String MESSAGE_JWT_EXPIRATION_NOT_POSITIVE =
      "jwt.expiration-minutes must be greater than zero.";

  private ConfigurationException(final String message) {
    super(message);
  }

  private ConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static ConfigurationException becauseLoadFailed(final Throwable cause) {
    return new ConfigurationException(MESSAGE_LOAD, cause);
  }

  public static ConfigurationException becauseJwtSecretIsTooShort(final int minimumBytes) {
    return new ConfigurationException(String.format(MESSAGE_JWT_SECRET_TOO_SHORT, minimumBytes));
  }

  public static ConfigurationException becauseJwtIssuerIsBlank() {
    return new ConfigurationException(MESSAGE_JWT_ISSUER_BLANK);
  }

  public static ConfigurationException becauseJwtExpirationIsNotPositive() {
    return new ConfigurationException(MESSAGE_JWT_EXPIRATION_NOT_POSITIVE);
  }
}
