package org.sotil.kuarkus.demo.application.ports.out;

import org.sotil.kuarkus.demo.infrastructure.repositories.CustomerRepositoryDao;

public interface CustomerRepoOutputPort extends CustomerRepositoryDao {

    boolean findTestPOR();

}
