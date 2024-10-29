package org.hr.exception.mapper;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hr.exception.EntityNotFoundException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class HumanResourceExceptionMapper implements ExceptionMapper<EntityNotFoundException.HumanResourceException> {

  @Override
  public Response toResponse(EntityNotFoundException.HumanResourceException exception) {
    return exception.getResponse();
  }
}
