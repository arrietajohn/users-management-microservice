package com.jcaa.usersmanagement.infrastructure.security;

public record IssuedToken(String value, long expiresInSeconds) {}
