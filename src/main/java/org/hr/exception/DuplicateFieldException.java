package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;


public class DuplicateFieldException extends HumanResourceException {

  public DuplicateFieldException(String fieldName) {
    super(fieldName, String.format("The %s field has already existed", fieldName), Response.Status.CONFLICT);
  }
}
