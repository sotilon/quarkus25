package org.sotil.kuarkus.demo.infrastructure.adapters.inbound.admin;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.*;
import org.sotil.kuarkus.demo.application.ports.in.CustomerCrudServicePort;
import org.sotil.kuarkus.demo.domain.models.Customer;

@GraphQLApi
public class CustomerAdminGraphQLResource {

  @Inject
  CustomerCrudServicePort customerServicePort;

  @Mutation
  public Uni<Customer> addCustomerMutation(Customer customer) {
    return customerServicePort.add(customer);
  }

  @Mutation
  public Uni<Boolean> deleteCustomer(Long id) {
    return customerServicePort.delete(id);
  }

}
