package org.hr.exception.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ErrorResponseBody(
  int status,
  String message,
  @JsonProperty(value = "time_stamp") LocalDateTime timeStamp) implements Serializable {

}
