package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateUserRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.LoginRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateUserRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.TokenRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.UserRestResponse;
import com.jcaa.usersmanagement.infrastructure.security.IssuedToken;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserRestMapper {

  private static final String TOKEN_TYPE_BEARER = "Bearer";

  public CreateUserCommand toCreateCommand(final CreateUserRestRequest request) {
    return new CreateUserCommand(
        request.id(),
        request.name(),
        request.email(),
        request.password(),
        request.role());
  }

  public UpdateUserCommand toUpdateCommand(final String id, final UpdateUserRestRequest request) {
    return new UpdateUserCommand(
        id,
        request.name(),
        request.email(),
        request.password(),
        request.role(),
        request.status());
  }

  public GetUserByIdQuery toGetByIdQuery(final String id) {
    return new GetUserByIdQuery(id);
  }

  public DeleteUserCommand toDeleteCommand(final String id) {
    return new DeleteUserCommand(id);
  }

  public UserRestResponse toResponse(final UserModel user) {
    return new UserRestResponse(
        user.getId().value(),
        user.getName().value(),
        user.getEmail().value(),
        user.getRole().name(),
        user.getStatus().name());
  }

  public List<UserRestResponse> toResponseList(final List<UserModel> users) {
    return users.stream().map(UserRestMapper::toResponse).toList();
  }

  public LoginCommand toLoginCommand(final LoginRestRequest request) {
    return new LoginCommand(request.email(), request.password());
  }

  public TokenRestResponse toTokenResponse(final IssuedToken token) {
    return new TokenRestResponse(token.value(), TOKEN_TYPE_BEARER, token.expiresInSeconds());
  }
}

