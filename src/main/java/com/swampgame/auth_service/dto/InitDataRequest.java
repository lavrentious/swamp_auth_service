package com.swampgame.auth_service.dto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotBlank;

public class InitDataRequest {
  @NotBlank
  private String data;
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public Map<String, String> getParams() {
    Map<String, String> params = new LinkedHashMap<>();
    if (data == null || data.isBlank()) {
      return params;
    }
    for (String pair : data.split("&")) {
      int idx = pair.indexOf('=');
      if (idx < 0)
        continue;
      String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
      String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
      params.put(key, value);
    }
    return params;
  }

  public static String getDatacheck(Map<String, String> params) {
    Map<String, String> copy = new LinkedHashMap<>(params);
    copy.remove("hash");
    String[] keys = copy.keySet().toArray(new String[0]);
    java.util.Arrays.sort(keys);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < keys.length; i++) {
      String k = keys[i];
      sb.append(k).append('=').append(Objects.toString(copy.get(k), ""));
      if (i < keys.length - 1) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  public TelegramUserDto getUser() {
    Map<String, String> params = getParams();
    String userJson = params.get("user");
    if (userJson == null || userJson.isBlank()) {
      return null;
    }
    try {
      return MAPPER.readValue(userJson, TelegramUserDto.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse Telegram user JSON", e);
    }
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}