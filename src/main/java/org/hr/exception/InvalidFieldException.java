package org.hr.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public final class InvalidFieldException extends HumanResourceException {

  public InvalidFieldException(String fieldName, String message) {
    super(
      fieldName,
      message,
      LocalDateTime.now()
    );
  }
}
