package org.sotil.kuarkus.demo.application.port.in;

import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductInputPort {
  List<Product> list();

  Optional<Product> getById(Long Id);

  Product add(Product p);

  Boolean delete(Long Id);

  Product update(Product p);

}
