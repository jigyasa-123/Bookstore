package com.netent.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.error.subdoc.PathNotFoundException;

@ControllerAdvice
public class ApplicationExceptionHandler {
  
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseTemplate> resourceNotFound(ResourceNotFoundException ex ){
        return new ResponseEntity<>(new ErrorResponseTemplate("Resource not found",ex.getMessage()),HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(DocumentNotFoundException.class)
  public ResponseEntity<ErrorResponseTemplate> documentNotFound(DocumentNotFoundException ex ){
    return new ResponseEntity<>(new ErrorResponseTemplate("Document not found",ex.getMessage()),HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(PathNotFoundException.class)
  public ResponseEntity<ErrorResponseTemplate> pathNotFound(PathNotFoundException ex ){
    return new ResponseEntity<>(new ErrorResponseTemplate("Path not found",ex.getMessage()),HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(RemoteServiceException.class)
  public ResponseEntity<ErrorResponseTemplate> remoteServiceException(RemoteServiceException ex ){
    return new ResponseEntity<>(new ErrorResponseTemplate(ex.getErrorMsg(),ex.getMessage()),HttpStatus.valueOf(ex.getHttpStatusCode()));
  }
  
  @ExceptionHandler(ServiceRequestTimeoutException.class)
  public ResponseEntity<ErrorResponseTemplate> serviceRequestTimeoutException(ServiceRequestTimeoutException ex ){
    if(ex.getHttpStatusCode()!=null)
      return new ResponseEntity<>(new ErrorResponseTemplate(ex.getErrorMsg(),ex.getMessage()),HttpStatus.valueOf(ex.getHttpStatusCode()));
    return new ResponseEntity<>(new ErrorResponseTemplate(ex.getErrorMsg(),ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseTemplate> argumentNotValid(MethodArgumentNotValidException ex ){
    return new ResponseEntity<>(new ErrorResponseTemplate("Bad Request",ex.getMessage()),HttpStatus.BAD_REQUEST);

  }
  
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponseTemplate> missingParameter(MissingServletRequestParameterException ex ){
    return new ResponseEntity<>(new ErrorResponseTemplate("Bad Request",ex.getMessage()),HttpStatus.BAD_REQUEST);

  }
  
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseTemplate> genericException(Exception ex ){
    return new ResponseEntity<>(new ErrorResponseTemplate("Internal Server Error",ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

  }

}
