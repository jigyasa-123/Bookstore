package com.netent.bookstore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.netent.bookstore.exception.RemoteServiceException;
import com.netent.bookstore.exception.ServiceRequestTimeoutException;

@ExtendWith(MockitoExtension.class)
public class ExternalCallServiceTest {
  
  @Mock
  private RestTemplate restTemplate;
  

  
  @InjectMocks
  private ExternalCallService service;
  
  @Test
  public void testCallMediaCoverageAPI() {
    service.url = "http://url";
    ResponseEntity<String> response = new ResponseEntity<String>("response",HttpStatus.OK);
    Mockito.when(restTemplate.exchange("http://url", HttpMethod.GET,null, String.class)).thenReturn(response);
    Assertions.assertEquals("response", service.callMediaCoverageAPI());
  }
  
  @Test
  public void testCallMediaCoverageAPIRemoteException() {
    service.url = "http://url";
    Mockito.when(restTemplate.exchange("http://url", HttpMethod.GET,null, String.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
    Assertions.assertThrows(RemoteServiceException.class, () -> service.callMediaCoverageAPI());
  }
  
  @Test
  public void testCallMediaCoverageAPIServiceTimeoutException() {
    service.url = "http://url";
    Mockito.when(restTemplate.exchange("http://url", HttpMethod.GET,null, String.class)).thenThrow(new HttpServerErrorException(HttpStatus.BAD_GATEWAY));
    Assertions.assertThrows(ServiceRequestTimeoutException .class, () -> service.callMediaCoverageAPI());
  }

}
