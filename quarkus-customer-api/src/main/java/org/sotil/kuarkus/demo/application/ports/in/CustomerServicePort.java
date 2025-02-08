package org.sotil.kuarkus.demo.application.ports.in;

import io.smallrye.mutiny.Uni;
import org.sotil.kuarkus.demo.domain.models.Customer;

import java.util.List;

public interface CustomerServicePort {

    Uni<List<Customer>> listCustomers();
    Uni<Customer> getById(Long Id);
    Uni<Customer> add(Customer c);
    Uni<Boolean> delete(Long Id);
    Uni<Customer> update(Long id, Customer c);
    Uni<List<Customer>> listUsingRepository();
    Uni<Customer> getByIdProduct(Long Id);

}
