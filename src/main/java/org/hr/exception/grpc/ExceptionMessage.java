package org.hr.exception.grpc;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hr.exception.HumanResourceException;

import java.time.LocalDateTime;

@RegisterForReflection
public record ExceptionMessage(
  String field,
  String message,
  LocalDateTime timeStamp
) {

  public static ExceptionMessage from(HumanResourceException exception) {
    return new ExceptionMessage(
      exception.getField(),
      exception.getMessage(),
      exception.getTimeStamp()
    );
  }
}
