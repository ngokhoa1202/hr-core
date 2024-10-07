package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class InvalidRequestBodyException extends HumanResourceException {

  public InvalidRequestBodyException() {
    super(
      "undefined",
      "The request body is invalid",
      Response.Status.BAD_REQUEST
    );
  }

  public InvalidRequestBodyException(String message) {
    super(
      "undefined",
      message,
      Response.Status.BAD_REQUEST
    );
  }

  public InvalidRequestBodyException(String fieldName, String entityName) {
    super(
      fieldName,
      String.format("The %s with %s is invalid", entityName, fieldName),
      Response.Status.BAD_REQUEST
    );
  }
}
