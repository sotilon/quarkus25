package org.sotil.kuarkus.demo.application.ports.out;

import org.sotil.kuarkus.demo.infrastructure.repositories.CustomerRepositoryDao;

public interface CustomerOutputPort extends CustomerRepositoryDao {

    boolean findTestPOR();

}
