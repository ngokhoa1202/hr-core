package org.hr.exception;

import java.time.LocalDateTime;

public final class UnauthorizedException extends HumanResourceException {

  public UnauthorizedException(String fieldName) {
    super(
      fieldName,
      String.format("The %s is unauthorized", fieldName),
      LocalDateTime.now()
    );
  }
}
