package com.netent.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ExternalCallService {

  @Autowired
  @Qualifier("restTemplate")
  RestTemplate restTemplate;

  @Value("${json.uri}")
  String URI;

  public String callMediaCoverageAPI() {
    try {
      return restTemplate
          .exchange(UriComponentsBuilder.fromHttpUrl(URI).toUriString(),
              HttpMethod.GET, null, String.class)
          .getBody();
    } catch (Exception e) {
      return null;
    }
  }

}
