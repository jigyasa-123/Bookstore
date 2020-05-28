/**
 * ************************************************************************** Copyright (C) Lowe's
 * Companies, Inc. All rights reserved. This file is for internal use only at Lowe's Companies, Inc.
 * *************************************************************************
 */
package com.netent.bookstore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.ObjectError;

/**
 * @author apgupta
 */
class RemoteServiceExceptionTest {

  @Mock
  private List<ObjectError> allErrors;

  @InjectMocks
  RemoteServiceException exceptionMock;

  /**
   *
   * Test getter and setter for ServiceRequestTimeoutException
   */
  @Test
  public void gettersAndSetters() {
    RemoteServiceException remoteServiceException =
        new RemoteServiceException(exceptionMock, 404, "errorResp", "testMeg","en");
    assertEquals(allErrors, remoteServiceException.getMessage(),
        "Error messages are NOT equal");
    assertNotNull(remoteServiceException.getEntity(), "Get entity returns null");
    assertNotNull(remoteServiceException.getHttpStatusCode(), "Get http status returns null");
    assertNotNull(remoteServiceException.getErrorResp(), "Get error response returns null");
    assertNotNull(remoteServiceException.getErrorMsg(), "Get error message returns null");
  }
}
