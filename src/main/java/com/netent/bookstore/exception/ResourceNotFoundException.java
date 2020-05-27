
package com.netent.bookstore.exception;

/**
 * This class is for ResourceNotFoundException
 * 
 * @author jgarg
 *
 */
public class ResourceNotFoundException extends RuntimeException {
  
  // Constant variable for LOGGER
  private static final long serialVersionUID = -4942845854761476097L;

  // The entity variable
  private final String entity;
  /**
   * 
   * @param e
   * @param entityName
   */
  public ResourceNotFoundException(Exception e, String entityName) {
    super(e);
    this.entity = entityName;
  }

  /**
   * 
   * @param entityName
   */
  public ResourceNotFoundException(String entityName) {
    this.entity = entityName;
  }

  /**
   * 
   * @return entity
   */
  public String getEntity() {
    return entity;
  }
  
}
