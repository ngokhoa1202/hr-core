package org.hr.exception.grpc;

import org.hr.exception.HumanResourceException;

import java.time.LocalDateTime;

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
