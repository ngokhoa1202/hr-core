package org.hr.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public final class UnauthorizedException extends HumanResourceException {

  public UnauthorizedException(String fieldName) {
    super(
      fieldName,
      String.format("The %s is unauthorized", fieldName),
      LocalDateTime.now()
    );
  }
}
