package org.hr.exception;

import java.time.LocalDateTime;

public final class InvalidRequestBodyException extends HumanResourceException {

  public InvalidRequestBodyException() {
    super(
      "undefined",
      "The request body is invalid",
      LocalDateTime.now()
    );
  }

  public InvalidRequestBodyException(String message) {
    super(
      "undefined",
      message,
      LocalDateTime.now()
    );
  }

  public InvalidRequestBodyException(String fieldName, String entityName) {
    super(
      fieldName,
      String.format("The %s with %s is invalid", entityName, fieldName),
      LocalDateTime.now()
    );
  }
}
