package org.sotil.kuarkus.demo.infrastructure.exception;


import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

  @Override
  public Response toResponse(NotFoundException exception) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Resource not found");
    errorResponse.put("message", exception.getMessage());

    return Response.status(Response.Status.NOT_FOUND)
      .entity(errorResponse)
      .type(MediaType.APPLICATION_JSON)
      .build();
  }
}
