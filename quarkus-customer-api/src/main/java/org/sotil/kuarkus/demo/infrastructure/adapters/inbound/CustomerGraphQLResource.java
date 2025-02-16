package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.*;
import org.sotil.kuarkus.demo.application.ports.in.CustomerCrudServicePort;
import org.sotil.kuarkus.demo.domain.models.Customer;

import java.util.List;

@GraphQLApi
public class CustomerGraphQLResource {

  @Inject
  CustomerCrudServicePort customerServicePort;

  @Query("allCustomers")
  @Description("Get all Customers from datada base postgresql ")
  public Uni<List<Customer>> getAllCustomers() {
    return customerServicePort.listCustomers();
  }

  @Query("customerById")
  @Description("Get all Customers find by Id from datada base postgresql ")
  public Uni<Customer> getCustomer(@Name("customerId") Long id) {
    return customerServicePort.getById(id);
  }

}
