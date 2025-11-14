package com.swampgame.auth_service.dto;

import lombok.Data;

@Data
public class TelegramUserDto {
    private Long id;
    private String first_name;
    private String last_name;
    private String username;
    private String language_code;
    private Boolean is_bot;
    private Boolean is_premium;
    private Boolean added_to_attachment_menu;
    private Boolean allows_write_to_pm;
    private String photo_url;
}
