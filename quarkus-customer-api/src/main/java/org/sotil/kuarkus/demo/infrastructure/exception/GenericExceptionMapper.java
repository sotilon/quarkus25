package org.sotil.kuarkus.demo.infrastructure.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.sotil.kuarkus.demo.domain.exceptions.CustomerException;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
  @Override
  public Response toResponse(Throwable exception) {
    if (exception instanceof CustomerException) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Not fond  (customizer)");
      errorResponse.put("message", exception.getMessage());
      return Response.status(Response.Status.NOT_FOUND)
        .entity(errorResponse)
        .type(MediaType.APPLICATION_JSON)
        .build();
    }

    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Internal Server Error (customizer)");
    errorResponse.put("message", exception.getMessage());

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
      .entity(errorResponse)
      .type(MediaType.APPLICATION_JSON)
      .build();
  }
}