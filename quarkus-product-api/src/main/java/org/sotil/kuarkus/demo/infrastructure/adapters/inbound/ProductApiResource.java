package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.sotil.kuarkus.demo.application.port.in.ProductInputPort;
import org.sotil.kuarkus.demo.domain.models.Product;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;


import java.util.List;
import java.util.Optional;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0"))
@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductApiResource {

  @Inject
  ProductInputPort productInputPort;

  @GET
  public List<Product> list() {
    log.info("Getting all products");
    return productInputPort.list();
  }

  @GET
  @Path("/findAll")
  public List<Product> findAll() {
    log.info("Getting findAll products");
    return productInputPort.list();
  }

  @GET
  @Path("/find/{productId}")
  public Optional<Product> getById(@PathParam("productId") Long productId) {
    log.info("Getting product by id :: {}", productId);
    return productInputPort.getById(productId);
  }

  @POST
  public Response add(Product p) {
    log.info("Adding new product :: {}", p);
    productInputPort.add(p);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    log.info("Deleting product by id :: {}", id);
    productInputPort.delete(id);
    return Response.ok().build();
  }

  @PUT
  public Response update(Product p) {
    log.info("Updating product:: {}", p);
    Optional<Product> productdb = productInputPort.getById(p.getId());
    if (productdb.isPresent()) {
      Product product = productdb.get();
      product.setCode(p.getCode());
      product.setName(p.getName());
      product.setDescription(p.getDescription());
      productInputPort.update(product);
      return Response.ok().build();
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }


}
