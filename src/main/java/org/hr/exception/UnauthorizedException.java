package org.hr.exception;

import jakarta.ws.rs.core.Response;
import org.hr.exception.mapper.HumanResourceException;

public class UnauthorizedException extends HumanResourceException {

  public UnauthorizedException() {
    super("The provided information is unauthorized", Response.Status.UNAUTHORIZED);
  }
}
