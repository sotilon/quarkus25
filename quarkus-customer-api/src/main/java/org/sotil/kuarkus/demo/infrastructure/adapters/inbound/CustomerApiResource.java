package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestPath;
import org.sotil.kuarkus.demo.application.ports.in.CustomerServicePort;
import org.sotil.kuarkus.demo.domain.models.Customer;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;

@Slf4j
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerApiResource {

    @Inject
    CustomerServicePort customerServicePort;

    @GET
    public Uni<List<Customer>> list() {
        return customerServicePort.listCustomers();
    }

    @GET
    @Path("/{Id}")
    public Uni<Customer> getById(@PathParam("Id") Long Id) {
        return customerServicePort.getById(Id);
    }

    @POST
    public Uni<Response> add(Customer c) {
        return customerServicePort.add(c)
                .replaceWith(Response.ok(c).status(CREATED)::build);
    }

    @DELETE
    @Path("/{Id}")
    public Uni<Response> delete(@PathParam("Id") Long id) {
        return customerServicePort.delete(id)
                .map(deleted ->deleted
                        ?Response.ok().status(NO_CONTENT).build()
                        :Response.ok().status(NOT_FOUND).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Long id, Customer c) {
        return customerServicePort.update(id, c)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @GET
    @Path("using-repository")
    public Uni<List<Customer>> listUsingRepository() {
        return customerServicePort.listUsingRepository();
    }

    @GET
    @Path("/{Id}/product")
    public Uni<Customer> getByIdProduct(@PathParam("Id") Long id) {
        return customerServicePort.getByIdProduct(id);
    }

}
