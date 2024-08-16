package org.hr.exception.mapper;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class HumanResourceExceptionMapper implements ExceptionMapper<HumanResourceException> {

  @Override
  public Response toResponse(HumanResourceException exception) {
    return exception.getResponse();
  }
}
