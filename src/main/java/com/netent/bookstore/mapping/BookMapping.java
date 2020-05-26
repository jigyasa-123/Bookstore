package com.netent.bookstore.mapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class BookMapping {

  protected static final String MISSING_ISBN =
      "MISSING_ISBN:`ISBN` field is mandatory and can not be missing or null.";

  protected static final String MISSING_TITLE =
      "MISSING_TITLE:`TITLE` field is mandatory and can not be missing or null.";

  protected static final String MISSING_AUTHOR =
      "MISSING_AUTHOR:`AUTHOR` field is mandatory and can not be missing or null.";

  protected static final String MISSING_PRICE =
      "MISSING_PRICE:`PRICE` field is mandatory and can not be missing or null.";

  @NotEmpty(message = MISSING_ISBN)
  private String isbn;

  @NotNull(message = MISSING_TITLE)
  private String title;

  @NotNull(message = MISSING_AUTHOR)
  private String author;

  @NotNull(message = MISSING_PRICE)
  private Integer price;
  
 

}
