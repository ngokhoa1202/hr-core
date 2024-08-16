package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class InvalidRequestBodyException extends HumanResourceException {

  public InvalidRequestBodyException() {
    super("The request body is invalid", Response.Status.BAD_REQUEST);
  }

  public InvalidRequestBodyException(String message) {
    super(message, Response.Status.BAD_REQUEST);
  }
}
