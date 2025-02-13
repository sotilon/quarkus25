package org.sotil.kuarkus.demo.infrastructure.adapters.outbound;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.sotil.kuarkus.demo.application.ports.out.ProductGraphQLClientPort;
import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.List;

@Slf4j
@Path("/customer/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductApiClientGraphQLResource {

  @Inject
  ProductGraphQLClientPort productGraphQLClientPort;

  @GET
  @Path("/products-grapql")
  @Blocking
  public List<Product> getProductsGraphQl() throws Exception {
    log.info("Get product GraphQl from dynamicGraphQLClient ");
    return productGraphQLClientPort.getProductsGrapQl();
  }

  @GET
  @Path("/{Id}/product-grapql")
  @Blocking
  public Product getByIdProductGrapQl(@PathParam("Id") Long Id) throws Exception {
    log.info("Get product by id dynamicGraphQLClient");
    return productGraphQLClientPort.getByIdProductGrapQl(Id);
  }

}
