package com.netent.bookstore.exception;

import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * Handles HTTP errors as thrown by remote services.
 * @author jgarg
 */
@Getter
public class RemoteServiceException extends RuntimeException {

  private static final long serialVersionUID = -15419048344839L;

  // Http status code with which service failed
  @Nullable private final Integer httpStatusCode;

  // Http error response
  @Nullable private final String errorResp;

  // Error message
  private final String errorMsg;

  // Entity looking for
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
  public RemoteServiceException(
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
