package org.hr.exception;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public final class DuplicateFieldException extends HumanResourceException {

  public DuplicateFieldException(String fieldName) {
    super(
      fieldName,
      String.format("The %s field has already existed", fieldName),
      LocalDateTime.now()
    );
  }
}
