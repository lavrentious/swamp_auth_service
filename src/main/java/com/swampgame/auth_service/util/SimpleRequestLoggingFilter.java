package com.swampgame.auth_service.util;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SimpleRequestLoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String method = request.getMethod();
    String uri = request.getRequestURI();

    logger.info("Incoming request: " + method + " " + uri);

    // Optional: log headers
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String header = headerNames.nextElement();
      logger.debug("Header: " + header + " = " + request.getHeader(header));
    }

    long start = System.currentTimeMillis();
    filterChain.doFilter(request, response);
    long duration = System.currentTimeMillis() - start;

    logger.info("Response for " + method + " " + uri + " -> " +
        response.getStatus() + " (" + duration + " ms)");
  }
}
