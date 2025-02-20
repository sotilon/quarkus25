package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.sotil.kuarkus.demo.application.port.in.ProductInputPort;
import org.sotil.kuarkus.demo.domain.exceptions.ProductException;
import org.sotil.kuarkus.demo.domain.models.Product;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;


import java.util.List;
import java.util.Optional;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0"))
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductApiResource {

  @Inject
  ProductInputPort productInputPort;

  @GET
  @Path("/products")
  public List<Product> list() {
    log.info("Getting all products");
    return Optional.ofNullable(productInputPort.list())
      .filter(products -> !products.isEmpty())
      .orElseThrow(() -> new ProductException("No products found in the database", Response.Status.NOT_FOUND.getStatusCode()));
  }

  @GET
  @Path("/findAll")
  public List<Product> findAll() {
    log.info("Getting findAll products");
    return productInputPort.list();
  }

  @GET
  @Path("/find/{productId}")
  public Product getById(@PathParam("productId") Long productId) {
    log.info("Getting product by id :: {}", productId);
    return Optional.ofNullable(productInputPort.getById(productId))
      .flatMap(product -> product) // Unwraps the Optional if not null
      .orElseThrow(() -> new ProductException("Product not found for ID: " + productId, Response.Status.NOT_FOUND.getStatusCode()));
  }

}
