package com.swampgame.auth_service.dto;

import lombok.Data;

@Data
public class TelegramChatDto {
  private Long id;
  private ChatType type;
  private String title;
  private String username;
  private String photo_url;
}
