package org.hr.exception.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.ExceptionHandler;
import io.quarkus.grpc.ExceptionHandlerProvider;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.hr.exception.*;


@ApplicationScoped
public class HumanResourceProviderHandler implements ExceptionHandlerProvider {

  @Override
  public <ReqT, RespT> ExceptionHandler<ReqT, RespT> createHandler(
    ServerCall.Listener<ReqT> listener, ServerCall<ReqT, RespT> serverCall, Metadata metadata
  ) {
    return new HumanResourceSecurityHandler<>(listener, serverCall, metadata);
  }


  @Override
  public Throwable transform(Throwable t)  {
    Log.infof("An exception has been thrown: %s", t.getMessage());
    try {
      if (! (t instanceof HumanResourceException)) {
        return new StatusRuntimeException(Status.INTERNAL.withDescription("Cannot parse json"));
      }

      String exceptionJson = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .writer()
        .withDefaultPrettyPrinter()
        .writeValueAsString(ExceptionMessage.from((HumanResourceException) t));

      switch (t) {
        case EntityNotFoundException ex -> {
          return new StatusRuntimeException(
            Status.NOT_FOUND.withCause(ex).withDescription(exceptionJson)
          );
        }

        case UnauthorizedException ex -> {
          return new StatusRuntimeException(Status.PERMISSION_DENIED.withDescription(exceptionJson));
        }

        case DuplicateFieldException ex -> {
          return new StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(exceptionJson));
        }

        case InvalidFieldException ex -> {
          return new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(exceptionJson));
        }

        case InvalidRequestBodyException ex -> {
          return new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(exceptionJson));
        }

        default -> {
          return new StatusRuntimeException(Status.UNIMPLEMENTED.withDescription(exceptionJson));
        }
      }
    } catch (JsonProcessingException e) {
      return new StatusRuntimeException(Status.INTERNAL.withDescription("Cannot parse json"));
    }

  }

  private static class HumanResourceSecurityHandler<A, B> extends ExceptionHandler<A, B> {

    public HumanResourceSecurityHandler(ServerCall.Listener<A> listener, ServerCall<A, B> call, Metadata metadata) {
      super(listener, call, metadata);
    }

    @Override
    protected void handleException(Throwable t, ServerCall<A, B> call, Metadata metadata) {
      StatusRuntimeException statusRuntimeException = (StatusRuntimeException) ExceptionHandlerProvider.toStatusException(t, true);
      Metadata trailers = (statusRuntimeException.getTrailers() != null) ? statusRuntimeException.getTrailers() : metadata;
      call.close(statusRuntimeException.getStatus(), trailers);
    }
  }



}
