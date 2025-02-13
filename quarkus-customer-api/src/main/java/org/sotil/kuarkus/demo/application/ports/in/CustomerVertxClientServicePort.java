package org.sotil.kuarkus.demo.application.ports.in;

import io.smallrye.mutiny.Uni;
import org.sotil.kuarkus.demo.domain.models.Customer;

public interface CustomerVertxClientServicePort {

  Uni<Customer> getByIdProduct(Long Id);

}
