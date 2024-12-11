package org.hr.exception;


import java.time.LocalDateTime;


public final class DuplicateFieldException extends HumanResourceException {

  public DuplicateFieldException(String fieldName) {
    super(
      fieldName,
      String.format("The %s field has already existed", fieldName),
      LocalDateTime.now()
    );
  }
}
