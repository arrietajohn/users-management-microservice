package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record StudentEntity(
        String id,
        String name,
        String lastName,
        String birthDate,
        int semester,
        String email,
        String gender,
        String phone,
        String program,
        String university,
        String createdAt,
        String updatedAt) {}
