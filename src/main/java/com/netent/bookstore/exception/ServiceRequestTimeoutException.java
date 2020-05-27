
package com.netent.bookstore.exception;

import org.springframework.lang.Nullable;

import lombok.Getter;

/**
 * Handles 408 exceptions, service failure like time out.
 *
 * @author dev
 */
@Getter
public class ServiceRequestTimeoutException extends RuntimeException {

  private static final long serialVersionUID = -15419048752687833L;

  // Http status code with which service failed
  @Nullable private final Integer httpStatusCode;

  // Http error response
  @Nullable private final String errorResp;

  // Error message
  private final String errorMsg;

  private final String entity;

  /**
   * Constructor for RemoteServiceException
   *
   * @param e the original exception
   * @param httpStatusCode http status code
   * @param errorResp error response
   * @param errorMsg error message
   * @param entity entity
   */
  public ServiceRequestTimeoutException(
      Exception e,
      @Nullable Integer httpStatusCode,
      @Nullable String errorResp,
      String errorMsg,
      String entity) {
    super(e);
    this.httpStatusCode = httpStatusCode;
    this.errorResp = errorResp;
    this.errorMsg = errorMsg;
    this.entity = entity;
  }
}
