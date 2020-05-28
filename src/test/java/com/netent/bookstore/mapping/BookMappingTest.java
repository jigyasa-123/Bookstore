package com.netent.bookstore.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class BookMappingTest {
  
  @Test
  public void testSuccessScenario() {

    BookMapping mapping = new BookMapping();
    mapping.setAuthor("sheldon");
    mapping.setTitle("fault"); 
    mapping.setPrice(500);
    mapping.setIsbn("1234");
    Assertions.assertEquals("sheldon", mapping.getAuthor());
    
  }
  


}
