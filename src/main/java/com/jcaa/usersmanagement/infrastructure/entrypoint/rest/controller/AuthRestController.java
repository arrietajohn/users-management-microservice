package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.LoginRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.TokenRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.UserRestMapper;
import com.jcaa.usersmanagement.infrastructure.security.IssuedToken;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController implements AuthRestControllerDocs {

  private final LoginUseCase loginUseCase;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/login")
  public TokenRestResponse login(@Valid @RequestBody final LoginRestRequest request) {
    final LoginCommand command = UserRestMapper.toLoginCommand(request);
    final UserModel user = loginUseCase.execute(command);
    final IssuedToken token = jwtTokenProvider.generate(user);
    return UserRestMapper.toTokenResponse(token);
  }
}
