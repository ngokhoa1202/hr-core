package org.hr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public sealed class HumanResourceException extends RuntimeException
  permits DuplicateFieldException, EntityNotFoundException, InvalidRequestBodyException, InvalidFieldException,
    UnauthorizedException {

  private String field;
  private String message;
  private LocalDateTime timeStamp;
}