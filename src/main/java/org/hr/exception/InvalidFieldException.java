package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class InvalidFieldException extends HumanResourceException {

  public InvalidFieldException(String property, String message) {
    super(
      property,
      message,
      Response.Status.BAD_REQUEST
    );
  }
}
