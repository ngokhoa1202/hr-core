package org.hr.exception;

import java.time.LocalDateTime;

public final class InvalidFieldException extends HumanResourceException {

  public InvalidFieldException(String fieldName, String message) {
    super(
      fieldName,
      message,
      LocalDateTime.now()
    );
  }
}
