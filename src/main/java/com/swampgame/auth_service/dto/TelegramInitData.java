package com.swampgame.auth_service.dto;

import lombok.Data;

@Data
public class TelegramInitData {
  private String query_id;
  private TelegramUserDto user;
  private TelegramUserDto receiver;
  private TelegramChatDto chat;
  private ChatType chat_type;
  private String chat_instance;
  private String start_param;
  private Integer can_send_after;

  private String auth_date;
  private String hash;

  private String signature;
}
