package org.sotil.kuarkus.demo.application.ports.out;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.sotil.kuarkus.demo.infrastructure.dto.ProductData;

import java.util.List;

@Path("/product")
@RegisterRestClient(baseUri = "stork://ms-quarkus-productb-api")
@Produces(MediaType.APPLICATION_JSON)
public interface ProductBApiClientStorkFallBackPort {

  @GET
  @Path("/findAll")
  Uni<List<ProductData>> getAllProducts();

}
