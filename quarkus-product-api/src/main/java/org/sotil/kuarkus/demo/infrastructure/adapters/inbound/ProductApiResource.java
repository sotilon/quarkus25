package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.sotil.kuarkus.demo.application.port.in.ProductInputPort;
import org.sotil.kuarkus.demo.domain.models.Product;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;


import java.util.List;
import java.util.Optional;

@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0"))
@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductApiResource {

    @Inject
    ProductInputPort productInputPort;

    @GET
    public List<Product> list() {
        return productInputPort.list();
    }

    @GET
    @Path("/{id}")
    public Optional<Product> getById(@PathParam("id") Long id) {
        return productInputPort.getById(id);
    }

    @POST
    public Response add(Product p) {
        productInputPort.add(p);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        productInputPort.delete(id);
        return Response.ok().build();
    }

    @PUT
    public Response update(Product p) {
        Optional<Product> productdb = productInputPort.getById(p.getId());
        if(productdb.isPresent()){
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
