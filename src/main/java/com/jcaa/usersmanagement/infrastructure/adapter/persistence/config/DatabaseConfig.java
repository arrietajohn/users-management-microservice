package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Locale;

/**
 * Configuración de conexión a base de datos.
 *
 * <p>El motor se selecciona con {@code db.vendor} ({@code mysql} o {@code postgresql}). Cada motor
 * exige su propio formato de URL JDBC y sus propios parámetros de SSL, por eso la construcción de la
 * URL se delega en un {@code switch} sobre el vendedor en lugar de usar una plantilla única.
 */
public record DatabaseConfig(
    String host,
    int port,
    String databaseName,
    String username,
    String password,
    String sslMode,
    String vendor) {

  public static final String VENDOR_MYSQL = "mysql";
  public static final String VENDOR_POSTGRESQL = "postgresql";

  private static final String MYSQL_URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?sslMode=%s&serverTimezone=UTC&allowPublicKeyRetrieval=true";

  private static final String POSTGRESQL_URL_TEMPLATE =
      "jdbc:postgresql://%s:%d/%s?sslmode=%s";

  private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

  /**
   * Constructor de compatibilidad: mantiene MySQL como motor por defecto cuando no se indica
   * vendedor.
   */
  public DatabaseConfig(
      final String host,
      final int port,
      final String databaseName,
      final String username,
      final String password,
      final String sslMode) {
    this(host, port, databaseName, username, password, sslMode, VENDOR_MYSQL);
  }

  public String buildJdbcUrl() {
    return switch (normalizedVendor()) {
      case VENDOR_POSTGRESQL ->
          String.format(POSTGRESQL_URL_TEMPLATE, host, port, databaseName, postgreSqlSslMode());
      default -> String.format(MYSQL_URL_TEMPLATE, host, port, databaseName, sslMode);
    };
  }

  public String driverClassName() {
    return switch (normalizedVendor()) {
      case VENDOR_POSTGRESQL -> POSTGRESQL_DRIVER;
      default -> MYSQL_DRIVER;
    };
  }

  private String normalizedVendor() {
    if (vendor == null || vendor.isBlank()) {
      return VENDOR_MYSQL;
    }
    return vendor.trim().toLowerCase(Locale.ROOT);
  }

  /**
   * PostgreSQL usa la clave {@code sslmode} en minúsculas y valores distintos a los de MySQL
   * ({@code DISABLED}/{@code REQUIRED}), así que el valor se traduce en lugar de interpolarse tal
   * cual.
   */
  private String postgreSqlSslMode() {
    if (sslMode == null || sslMode.isBlank()) {
      return "disable";
    }
    return switch (sslMode.trim().toUpperCase(Locale.ROOT)) {
      case "DISABLED", "DISABLE" -> "disable";
      case "VERIFY_CA" -> "verify-ca";
      case "VERIFY_FULL", "VERIFY-FULL" -> "verify-full";
      default -> "require";
    };
  }
}