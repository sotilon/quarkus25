package org.sotil.kuarkus.demo.application.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.sotil.kuarkus.demo.application.ports.in.CustomerCrudServicePort;
import io.quarkus.hibernate.reactive.panache.Panache;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.sotil.kuarkus.demo.application.ports.out.CustomerRepoOutputPort;
import org.sotil.kuarkus.demo.application.ports.out.ProductApiClientStorkPort;
import org.sotil.kuarkus.demo.domain.models.Customer;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerCrudUseCaseImpl implements CustomerCrudServicePort {

  @Inject
  CustomerRepoOutputPort customerOutputPort;

  @RestClient
  ProductApiClientStorkPort productApiClientStorkPort;


  @WithSession
  @Override
  public Uni<List<Customer>> listCustomers() {
    //return Customer.listAll(Sort.by("names"));
    log.info("find Customers from database");
    customerOutputPort.findTestRepo();
    return customerOutputPort.listAll();
  }

  @WithSessionOnDemand
  @Override
  public Uni<Customer> getById(Long Id) {
    return Customer.findById(Id);
  }

  @WithTransaction
  @Override
  public Uni<Customer> add(Customer c) {
    c.getProducts().forEach(p -> p.setCustomer(c));
    return Panache.withTransaction(c::persist)
      .replaceWith(c);
  }

  @WithTransaction
  @Override
  public Uni<Boolean> delete(Long Id) {
    return Customer.deleteById(Id);
  }

  @WithTransaction
  @Override
  public Uni<Customer> update(Long id, Customer c) {
    return Panache
      .withTransaction(() -> Customer.<Customer>findById(id)
        .onItem().ifNotNull().invoke(entity -> {
          entity.setNames(c.getNames());
          entity.setAccountNumber(c.getAccountNumber());
          entity.setCode(c.getCode());
        })
      )
      .replaceWith(c);
  }

}

