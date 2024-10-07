package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class EntityNotFoundException extends HumanResourceException {

  public EntityNotFoundException(String fieldName, String entityName) {
    super(fieldName, String.format("The %s is not found", entityName), Response.Status.NOT_FOUND);
  }
}
