package com.netent.bookstore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.ObjectError;


public class ResourceNotFoundExceptionTest {

  @Mock
  private List<ObjectError> allErrors;

  @InjectMocks
  ResourceNotFoundException resourceNotFoundExceptionMock;

  @Test
  public void gettersAndSetters() {
    ResourceNotFoundException resourceNotFoundException =
        new ResourceNotFoundException("RESOURCE_NOT_FOUND");
    ResourceNotFoundException resourceNotFoundExceptionWithMock =
        new ResourceNotFoundException(resourceNotFoundExceptionMock, "en");
    assertEquals(allErrors, resourceNotFoundException.getMessage(),
        "Error messages are NOT equal");
    assertNotNull(resourceNotFoundException.getEntity(),
        "Get Entity call failed");
    assertNotNull(resourceNotFoundExceptionWithMock,
        "ResourceNotFoundException is null");
  }
}
