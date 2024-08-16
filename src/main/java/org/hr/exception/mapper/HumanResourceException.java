package org.hr.exception.mapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;

public class HumanResourceException extends WebApplicationException {

  public HumanResourceException(final String message, final Response.Status status) {

    super(Response.status(status)
      .entity(new ErrorResponseBody(status.getStatusCode(), message, LocalDateTime.now()))
      .type(MediaType.APPLICATION_JSON_TYPE)
      .build()
    );
  }
}
