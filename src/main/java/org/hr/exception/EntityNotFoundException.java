package org.hr.exception;

import java.time.LocalDateTime;

public final class EntityNotFoundException extends HumanResourceException {

  public EntityNotFoundException(String fieldName, String entityName) {
    super(
      fieldName,
      String.format("The %s is not found", entityName),
      LocalDateTime.now()
    );
  }

}
