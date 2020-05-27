package com.netent.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseTemplate {

  private String code;

  private String message;

  public ErrorResponseTemplate(String code, String message) {
    this.code = code;
    this.message = message;
  }

}
