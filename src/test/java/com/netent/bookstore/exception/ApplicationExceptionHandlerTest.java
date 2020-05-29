package com.netent.bookstore.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.error.subdoc.PathNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ApplicationExceptionHandlerTest {

  public ApplicationExceptionHandler handler;

  @Mock
  ResourceNotFoundException resourceNotFound;

  @Mock
  DocumentNotFoundException documentNotFound;

  @Mock
  PathNotFoundException pathNotFound;

  @Mock
  ServiceRequestTimeoutException serviceTiemout;

  @Mock
  RemoteServiceException remoteService;

  @BeforeEach
  public void setup() {
    handler = new ApplicationExceptionHandler();
  }

  @Test
  public void testResourcerNotFound() {
    ErrorResponseTemplate error =
        handler.resourceNotFound(resourceNotFound).getBody();
    Assertions.assertEquals("Resource not found", error.getCode());
  }

  @Test
  public void testDocumentNotFound() {
    Assertions.assertNotNull(handler.documentNotFound(documentNotFound));
  }

  @Test
  public void testPathNotFound() {
    Assertions.assertNotNull(handler.pathNotFound(pathNotFound));
  }

  @Test
  public void testServiceTimoeu() {
    Mockito.when(serviceTiemout.getHttpStatusCode()).thenReturn(400);
    Assertions
        .assertNotNull(handler.serviceRequestTimeoutException(serviceTiemout));
  }

  @Test
  public void testRemoteService() {
    Mockito.when(remoteService.getHttpStatusCode()).thenReturn(400);
    Assertions.assertNotNull(handler.remoteServiceException(remoteService));
  }
}
