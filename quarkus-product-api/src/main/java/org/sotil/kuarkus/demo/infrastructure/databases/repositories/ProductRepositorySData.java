package org.sotil.kuarkus.demo.infrastructure.databases.repositories;

import org.sotil.kuarkus.demo.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositorySData extends JpaRepository<Product,Long> {


}
