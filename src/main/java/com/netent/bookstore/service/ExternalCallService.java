package com.netent.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netent.bookstore.exception.RemoteServiceException;
import com.netent.bookstore.exception.ServiceRequestTimeoutException;

/**
 * Service class to call externaal API
 * @author jgarg
 *
 */
@Component
public class ExternalCallService {

  @Autowired
  @Qualifier("restTemplate")
  RestTemplate restTemplate;

  /**
   * JSON Api Url
   */
  @Value("${json.uri}")
  String url;

  private static String ERROR_MSG = "REMOTE SERVICE FAILED";

  /**
   * Get Posts from API
   * @return response from API
   */
  public String callMediaCoverageAPI() {
    try {
      return restTemplate
          .exchange(UriComponentsBuilder.fromHttpUrl(url).toUriString(),
              HttpMethod.GET, null, String.class)
          .getBody();
    } catch (HttpClientErrorException e) {
      //Handle client side exception
      throw new RemoteServiceException(e, e.getRawStatusCode(),
          e.getResponseBodyAsString(), ERROR_MSG + e.getMessage(), url);
    } catch (HttpServerErrorException e) {
      //Handle server side exception
      throw new ServiceRequestTimeoutException(e, e.getRawStatusCode(),
          e.getResponseBodyAsString(), ERROR_MSG + e.getMessage(), url);
    } catch (RestClientException e) {
      //Handle rest client exception
      throw new RemoteServiceException(e, null, null,
          ERROR_MSG + e.getMessage(), url);
    }
  }

}
