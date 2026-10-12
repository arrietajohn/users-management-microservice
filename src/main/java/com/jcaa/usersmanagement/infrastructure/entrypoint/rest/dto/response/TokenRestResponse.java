package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

public record TokenRestResponse(String accessToken, String tokenType, long expiresIn) {}
