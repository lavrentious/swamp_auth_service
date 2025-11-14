package com.swampgame.auth_service;

import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String SIGNATURE_KEY = "WebAppData";
  @Value("${bot.token}")
  private String botToken;

  public boolean authorize(Map<String, String> params, String datacheck) {
    if (params == null || params.isEmpty() || datacheck == null || datacheck.isBlank()) {
      return false;
    }
    System.out.println("Params:");
    for (Map.Entry<String, String> entry : params.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
    String hash = params.get("hash");
    if (hash == null || hash.isBlank()) {
      return false;
    }
    try {
      byte[] expected = hmacSha256(
          hmacSha256(SIGNATURE_KEY.getBytes(), this.botToken.getBytes()), datacheck.getBytes());
      String expectedHex = bytesToHex(expected);
      return expectedHex.equals(hash);
    } catch (Exception e) {
      return false;
    }
  }

  private static byte[] hmacSha256(byte[] key, byte[] data) throws Exception {
    Mac mac = Mac.getInstance(HMAC_ALGORITHM);
    mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
    return mac.doFinal(data);
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

}
