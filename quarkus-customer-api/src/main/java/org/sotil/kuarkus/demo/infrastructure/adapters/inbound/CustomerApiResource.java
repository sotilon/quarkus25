package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.sotil.kuarkus.demo.application.ports.in.CustomerCrudServicePort;
import org.sotil.kuarkus.demo.application.ports.in.CustomerStorkFallClientServicePort;
import org.sotil.kuarkus.demo.application.ports.in.CustomerVertxClientServicePort;
import org.sotil.kuarkus.demo.domain.exceptions.CustomerException;
import org.sotil.kuarkus.demo.domain.models.Customer;

import java.util.List;

@Slf4j
@Path("/api/v1")
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
  @Path("/customers")
  public Uni<List<Customer>> list() {
      log.info("Getting all customers reactive ");
    try{
      return customerServicePort.listCustomers();
    }catch (Exception exception){
      log.info("ERROR::{}",exception.getMessage());
      throw new CustomerException("Customer not found in the database", Response.Status.NOT_FOUND.getStatusCode());
    }
  }

  @GET
  @Path("/customer/{Id}")
  public Uni<Customer> getById(@PathParam("Id") Long Id) {
    log.info("Getting customer by id ::{}", Id);
    return customerServicePort.getById(Id);
  }

  @GET
  @Path("/customer/{Id}/product")
  public Uni<Customer> getByIdProduct(@PathParam("Id") Long id) {
    log.info("Getting Product by id using Reactivo id :: {}", id);
    return customerVertxClientServicePort.getByIdProduct(id);
  }

  @GET
  @Path("/customer/findById/{customerId}")
  public Uni<Customer> findDetailProductByCustomerId(@PathParam("customerId") Long customerId) {
    log.info("Getting Product detail fill by customer id using client :: {}", customerId);
    return customerStorkClientServicePort.listUsingRepository(customerId);
  }

  @GET
  @Path("/customer/findProductFillByCustomerId/{customerId}")
  public Uni<Customer> findProductFillByCustomerId(@PathParam("customerId") Long customerId) {
    log.info("Getting Product detail by customer id using client :: {}", customerId);
    return customerStorkClientServicePort.findCustomerWithProductFill(customerId);
  }

}
