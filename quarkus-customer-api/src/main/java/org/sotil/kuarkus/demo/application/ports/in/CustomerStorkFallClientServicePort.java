package org.sotil.kuarkus.demo.application.ports.in;

import io.smallrye.mutiny.Uni;
import org.sotil.kuarkus.demo.domain.models.Customer;

public interface CustomerStorkFallClientServicePort {

  Uni<Customer> listUsingRepository(Long id);

  Uni<Customer> findCustomerWithProductFill(Long customerId);

}
