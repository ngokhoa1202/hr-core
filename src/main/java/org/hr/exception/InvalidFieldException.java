package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class InvalidFieldException extends HumanResourceException {

  public InvalidFieldException(String message) {
    super(message, Response.Status.BAD_REQUEST);
  }
}
