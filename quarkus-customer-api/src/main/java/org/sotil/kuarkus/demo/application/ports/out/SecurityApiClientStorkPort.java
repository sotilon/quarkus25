package org.sotil.kuarkus.demo.application.ports.out;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.sotil.kuarkus.demo.application.ports.dtos.UserDTO;
import org.sotil.kuarkus.demo.infrastructure.dto.ProductData;

import java.util.List;

/**
 * GET http://localhost:8081/api/v1/products
 */
@Path("/api/v1")
@RegisterRestClient(baseUri = "stork://ms-quarkus-security-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SecurityApiClientStorkPort {

  @POST
  @Path("/admin/login")
  public Uni<Response> getTokeJwt(UserDTO user);

}
