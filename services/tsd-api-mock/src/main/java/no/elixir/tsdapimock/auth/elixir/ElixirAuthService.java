package no.elixir.tsdapimock.auth.elixir;

import static javax.management.timer.Timer.ONE_HOUR;

import no.elixir.tsdapimock.auth.elixir.dto.ElixirTokenRequestDto;
import no.elixir.tsdapimock.auth.elixir.dto.ElixirTokenResponseDto;
import no.elixir.tsdapimock.exceptions.CredentialsMismatchException;
import no.elixir.tsdapimock.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElixirAuthService {
  private final JwtService jwtService;

  @Autowired
  public ElixirAuthService(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  public ElixirTokenResponseDto getToken(
      String project, String authorizationHeader, ElixirTokenRequestDto request) {
    if (!authorizationHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Invalid authorization header");
    }
    if (!jwtService.verify(authorizationHeader)) {
      throw new CredentialsMismatchException("Invalid authorization token");
    }
    var userName = jwtService.getSubject(request.token());
    var token = jwtService.createJwt(project, userName, "TSD", userName, ONE_HOUR);
    return new ElixirTokenResponseDto(token);
  }
}
