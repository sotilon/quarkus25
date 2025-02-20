package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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
    log.info("Getting all customers reactively");
    return customerServicePort.listCustomers()
      .onItem().ifNull().failWith(new CustomerException("Customer list is null", Response.Status.NOT_FOUND.getStatusCode()))
      .onItem().transform(customers -> {
        if (customers.isEmpty()) {
          throw new CustomerException("Customer list is empty", Response.Status.NOT_FOUND.getStatusCode());
        }
        return customers;
      })
      .onFailure().invoke(exception -> log.error("ERROR::{}", exception.getMessage()));
  }

  @GET
  @Path("/customer/{Id}")
  public Uni<Customer> getById(@PathParam("Id") Long Id) {
    log.info("Getting customer by id ::{}", Id);
    return customerServicePort.getById(Id)
      .onItem().ifNull().failWith(new CustomerException("Customer not found for ID: " + Id, Response.Status.NOT_FOUND.getStatusCode()))
      .onItem().transform(customer -> {
        if (customer.id == null) { // Assuming ID should always be present
          throw new CustomerException("Customer not found for ID: " + Id, Response.Status.NOT_FOUND.getStatusCode());
        }
        return customer;
      })
      .onFailure().invoke(exception -> log.error("ERROR:: {}", exception.getMessage()));
  }

  @GET
  @Path("/customer/{Id}/product")
  public Uni<Customer> getByIdProduct(@PathParam("Id") Long id) {
    log.info("Getting Customer by id using Reactivo id :: {}", id);
    return customerVertxClientServicePort.getByIdProduct(id)
      .onItem().ifNull().failWith(new CustomerException("Customer not found for ID: " + id, Response.Status.NOT_FOUND.getStatusCode()))
      .onItem().transform(customer -> {
        if (customer.id == null) { // Assuming ID should always be present
          throw new CustomerException("Customer not found for ID: " + id, Response.Status.NOT_FOUND.getStatusCode());
        }
        return customer;
      })
      .onFailure().invoke(exception -> log.error("ERROR:: {}", exception.getMessage()));
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
    return customerStorkClientServicePort.findCustomerWithProductFill(customerId)
      .onItem().ifNull().failWith(new CustomerException("No Customer details found for customer ID: " + customerId, Response.Status.NOT_FOUND.getStatusCode()))
      .onItem().transform(customer -> {
        if (customer.id == null) { // Assuming ID should always be present
          throw new CustomerException("No product details found for customer ID: " + customerId, Response.Status.NOT_FOUND.getStatusCode());
        }
        return customer;
      })
      .onFailure().invoke(exception -> log.error("ERROR:: {}", exception.getMessage()));
  }

}
