package org.hr.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public final class EntityNotFoundException extends HumanResourceException {

  public EntityNotFoundException(String fieldName, String entityName) {
    super(
      fieldName,
      String.format("The %s is not found", entityName),
      LocalDateTime.now()
    );
  }

}
