package org.sotil.kuarkus.demo.infrastructure.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import org.sotil.kuarkus.demo.domain.models.Customer;

public interface CustomerRepositoryDao extends PanacheRepositoryBase<Customer, Long> {

  String findTestRepo();

}
