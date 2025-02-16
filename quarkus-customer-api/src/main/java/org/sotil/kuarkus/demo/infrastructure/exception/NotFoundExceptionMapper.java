package org.sotil.kuarkus.demo.infrastructure.exception;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.sotil.kuarkus.demo.domain.exceptions.CustomerException;

@ApplicationScoped
public class NotFoundExceptionMapper implements ExceptionMapper<CustomerException> {
  @Override
  public Response toResponse(CustomerException exception) {
    return Response.status(Response.Status.NOT_FOUND)
      .entity("{\"error\": \"" + exception.getMessage() + "\"}")
      .header("Content-Type", "application/json")
      .build();
  }

  public Response toResponse(NotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND)
      .entity("{\"error\": \"" + exception.getMessage() + "\"}")
      .header("Content-Type", "application/json")
      .build();
  }
}
