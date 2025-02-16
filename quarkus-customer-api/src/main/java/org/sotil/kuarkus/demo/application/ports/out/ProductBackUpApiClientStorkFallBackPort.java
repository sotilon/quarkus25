package org.sotil.kuarkus.demo.application.ports.out;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.sotil.kuarkus.demo.infrastructure.dto.ProductData;

import java.util.List;

/**
 * GET http://localhost:8081/api/v1/products
 */
@Path("/api/v1")
@RegisterRestClient(baseUri = "stork://ms-quarkus-product-backup-api")
@Produces(MediaType.APPLICATION_JSON)
public interface ProductBackUpApiClientStorkFallBackPort {

  @GET
  @Path("/products")
  Uni<List<ProductData>> getAllProducts();

}
