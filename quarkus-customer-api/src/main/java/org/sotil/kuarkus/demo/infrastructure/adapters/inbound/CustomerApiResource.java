package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestPath;
import org.sotil.kuarkus.demo.application.ports.in.CustomerCrudServicePort;
import org.sotil.kuarkus.demo.application.ports.in.CustomerStorkFallClientServicePort;
import org.sotil.kuarkus.demo.application.ports.in.CustomerVertxClientServicePort;
import org.sotil.kuarkus.demo.domain.models.Customer;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;

@Slf4j
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerApiResource {

  @Inject
  CustomerCrudServicePort customerServicePort;

  @Inject
  CustomerStorkFallClientServicePort customerStorkClientServicePort;

  @Inject
  CustomerVertxClientServicePort customerVertxClientServicePort;

  @GET
  public Uni<List<Customer>> list() {
    log.info("Getting all customers reactive ");
    return customerServicePort.listCustomers();
  }

  @GET
  @Path("/{Id}")
  public Uni<Customer> getById(@PathParam("Id") Long Id) {
    log.info("Getting customer by id ::{}", Id);
    return customerServicePort.getById(Id);
  }

  @POST
  public Uni<Response> add(Customer c) {
    log.info("Saving Customer ::{}", c);
    return customerServicePort.add(c)
      .replaceWith(Response.ok(c).status(CREATED)::build);
  }

  @DELETE
  @Path("/{Id}")
  public Uni<Response> delete(@PathParam("Id") Long id) {
    log.info("deleting by id :: {}", id);
    return customerServicePort.delete(id)
      .map(deleted -> deleted
        ? Response.ok().status(NO_CONTENT).build()
        : Response.ok().status(NOT_FOUND).build());
  }

  @PUT
  @Path("{id}")
  public Uni<Response> update(@RestPath Long id, Customer c) {
    log.info("Updating customer id ::{}", id);
    return customerServicePort.update(id, c)
      .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
      .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
  }

  @GET
  @Path("/{Id}/product")
  public Uni<Customer> getByIdProduct(@PathParam("Id") Long id) {
    log.info("Getting Product by id using Reactivo id :: {}", id);
    return customerVertxClientServicePort.getByIdProduct(id);
  }

  @GET
  @Path("findCustomerById/{customerId}")
  public Uni<Customer> findDetailProductByCustomerId(@PathParam("customerId") Long customerId) {
    log.info("Getting Product detail by customer id using client :: {}", customerId);
    return customerStorkClientServicePort.listUsingRepository(customerId);
  }

  @GET
  @Path("findProductFillByCustomerId/{customerId}")
  public Uni<Customer> findProductFillByCustomerId(@PathParam("customerId") Long customerId) {
    log.info("Getting Product detail by customer id using client :: {}", customerId);
    return customerStorkClientServicePort.findCustomerWithProductFill(customerId);
  }

}
