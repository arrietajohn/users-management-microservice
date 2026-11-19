package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.HealthRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

  private final BuildProperties buildProperties;

  public HealthRestController(final BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  @Operation(summary = "Estado del servicio y metadatos del proyecto")
  @GetMapping("/health")
  public HealthRestResponse health() {
    return new HealthRestResponse("UP", buildProperties.getName(), buildProperties.getVersion());
  }
}
