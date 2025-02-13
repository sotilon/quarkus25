package org.sotil.kuarkus.demo.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.sotil.kuarkus.demo.application.ports.out.CustomerRepoOutputPort;


@ApplicationScoped
public class CustomerRepository implements CustomerRepositoryDao, CustomerRepoOutputPort {

  @Override
  public String findTestRepo() {
    return "";
  }

  @Override
  public boolean findTestPOR() {
    return false;
  }
}
