package com.swampgame.auth_service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swampgame.auth_service.dto.InitDataRequest;
import com.swampgame.auth_service.dto.JwtPayload;
import com.swampgame.auth_service.dto.TelegramUserDto;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
  private final AuthService authService;
  private final JwtService jwtService;

  public AuthController(AuthService authService, JwtService jwtService) {
    this.authService = authService;
    this.jwtService = jwtService;
  }

  @GetMapping({ "/", "" })
  public String healthCheck() {
    System.err.println("health");
    return "OK";
  }

  @PostMapping({ "/telegram", "/telegram/" })
  public ResponseEntity<?> authorize(@RequestBody InitDataRequest body) {
    // 0. read params
    var params = body.getParams();
    if (params.isEmpty()) {
      return ResponseEntity.badRequest().body("Invalid init data");
    }

    // 1. validate
    String datacheck = InitDataRequest.getDatacheck(params);
    boolean ok = this.authService.authorize(params, datacheck);
    if (!ok) {
      return ResponseEntity.status(401).body("Invalid hash");
    }

    // 2. issue a jwt
    TelegramUserDto telegramUser = body.getUser();
    if (telegramUser == null) {
      return ResponseEntity.status(400).body("Missing Telegram user info");
    }
    JwtPayload payload = new JwtPayload();
    payload.setId(0); // TODO: find user (swamp user) by telegram user id
    payload.setTelegramUser(telegramUser);
    String jwt = this.jwtService.issue(payload);
    return ResponseEntity.ok(jwt);
  }

  // validate jwt
  @PostMapping({ "/validate", "/validate/" })
  public ResponseEntity<?> validateJwt(@RequestBody String jwt) {
    try {
      this.jwtService.validate(jwt);
    } catch (Exception e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
    return ResponseEntity.ok("OK");
  }

}
