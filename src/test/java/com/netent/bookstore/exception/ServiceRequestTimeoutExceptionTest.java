
package com.netent.bookstore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.ObjectError;


class ServiceRequestTimeoutExceptionTest {

  @Mock
  private List<ObjectError> allErrors;

  @InjectMocks
  ServiceRequestTimeoutException exceptionMock;

  /**
   *
   * Test getter and setter for ServiceRequestTimeoutException
   */
  @Test
  public void gettersAndSetters() {
    ServiceRequestTimeoutException serviceRequestTimeoutException =
        new ServiceRequestTimeoutException(exceptionMock, 404, "dummyErrorResp", "testMsg","en");
    assertEquals(allErrors, serviceRequestTimeoutException.getMessage(),
        "Error messages are NOT equal");
    assertNotNull(serviceRequestTimeoutException.getEntity(), "Get entity returns null");
    assertNotNull(serviceRequestTimeoutException.getHttpStatusCode(), "Get http status returns null");
    assertNotNull(serviceRequestTimeoutException.getErrorResp(), "Get error response returns null");
    assertNotNull(serviceRequestTimeoutException.getErrorMsg(), "Get error message returns null");
  }
}
