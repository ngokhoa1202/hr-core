package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class UnauthorizedException extends HumanResourceException {

  public UnauthorizedException(String fieldName) {
    super(
      fieldName,
      String.format("The %s is unauthorized", fieldName),
      Response.Status.UNAUTHORIZED
    );
  }
}
