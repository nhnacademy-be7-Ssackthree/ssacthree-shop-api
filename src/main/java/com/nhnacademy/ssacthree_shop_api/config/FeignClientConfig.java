package com.nhnacademy.ssacthree_shop_api.config;

import feign.RequestInterceptor;
import java.util.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class FeignClientConfig {

  @Value("${elasticsearch.username}")
  private String username;

  @Value("${elasticsearch.password}")
  private String password;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      String auth = username + ":" + password;
      String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
      requestTemplate.header("Authorization", "Basic " + encodedAuth);
    };
  }
}
