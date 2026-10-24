package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DatabaseConfig")
class DatabaseConfigTest {

  private static final String HOST = "mysql.example.com";
  private static final int PORT = 15425;
  private static final String DATABASE = "crud_usuarios";
  private static final String USERNAME = "avnadmin";
  private static final String PASSWORD = "secret";
  private static final String SSL_MODE = "REQUIRED";

  @Test
  @DisplayName("buildJdbcUrl() usa la plantilla de MySQL cuando no se indica vendedor")
  void shouldBuildJdbcUrlWithConfiguredSslMode() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig(HOST, PORT, DATABASE, USERNAME, PASSWORD, SSL_MODE);

    // Act
    final String jdbcUrl = config.buildJdbcUrl();

    // Assert
    assertThat(jdbcUrl)
        .isEqualTo(
            "jdbc:mysql://mysql.example.com:15425/crud_usuarios"
                + "?sslMode=REQUIRED&serverTimezone=UTC&allowPublicKeyRetrieval=true");
  }

  @Test
  @DisplayName("buildJdbcUrl() usa la plantilla de PostgreSQL cuando vendor=postgresql")
  void shouldBuildPostgreSqlJdbcUrl() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig(
            HOST,
            PORT,
            DATABASE,
            USERNAME,
            PASSWORD,
            SSL_MODE,
            DatabaseConfig.VENDOR_POSTGRESQL);

    // Act
    final String jdbcUrl = config.buildJdbcUrl();

    // Assert
    assertThat(jdbcUrl)
        .isEqualTo(
            "jdbc:postgresql://mysql.example.com:15425/crud_usuarios?sslmode=require");
  }

  @Test
  @DisplayName("driverClassName() devuelve el driver que corresponde a cada motor")
  void shouldResolveDriverClassNamePerVendor() {
    // Arrange
    final DatabaseConfig mysqlConfig =
        new DatabaseConfig(HOST, PORT, DATABASE, USERNAME, PASSWORD, SSL_MODE);
    final DatabaseConfig postgresConfig =
        new DatabaseConfig(
            HOST, PORT, DATABASE, USERNAME, PASSWORD, SSL_MODE, DatabaseConfig.VENDOR_POSTGRESQL);

    // Act + Assert
    assertThat(mysqlConfig.driverClassName()).isEqualTo("com.mysql.cj.jdbc.Driver");
    assertThat(postgresConfig.driverClassName()).isEqualTo("org.postgresql.Driver");
  }

  @Test
  @DisplayName("buildJdbcUrl() traduce el modo SSL de MySQL al valor propio de PostgreSQL")
  void shouldTranslateSslModeForPostgreSql() {
    // Arrange
    final DatabaseConfig disabled =
        new DatabaseConfig(
            HOST,
            PORT,
            DATABASE,
            USERNAME,
            PASSWORD,
            "DISABLED",
            DatabaseConfig.VENDOR_POSTGRESQL);
    final DatabaseConfig verifyFull =
        new DatabaseConfig(
            HOST,
            PORT,
            DATABASE,
            USERNAME,
            PASSWORD,
            "VERIFY_FULL",
            DatabaseConfig.VENDOR_POSTGRESQL);

    // Act + Assert
    assertThat(disabled.buildJdbcUrl()).endsWith("?sslmode=disable");
    assertThat(verifyFull.buildJdbcUrl()).endsWith("?sslmode=verify-full");
  }
}