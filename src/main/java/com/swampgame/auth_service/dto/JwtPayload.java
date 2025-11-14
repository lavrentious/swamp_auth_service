package com.swampgame.auth_service.dto;

import lombok.Data;

@Data
public class JwtPayload {
  private long id;
  private TelegramUserDto telegramUser;
}
