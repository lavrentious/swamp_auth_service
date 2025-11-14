package com.swampgame.auth_service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swampgame.auth_service.dto.InitDataRequest;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping({ "/", "" })
  public String healthCheck() {
    System.err.println("health");
    return "OK";
  }

  @PostMapping({ "/telegram", "/telegram/" })
  public ResponseEntity<?> authorize(@RequestBody InitDataRequest body) {
    var params = body.getParams();
    if (params.isEmpty()) {
      return ResponseEntity.badRequest().body("Invalid init data");
    }

    String datacheck = InitDataRequest.getDatacheck(params);
    boolean ok = this.authService.authorize(params, datacheck);

    if (!ok) {
      return ResponseEntity.status(401).body("Invalid hash");
    }

    return ResponseEntity.ok("Authorized");
  }
}
