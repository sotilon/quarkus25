package org.sotil.kuarkus.demo.application.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.sotil.kuarkus.demo.application.port.in.ProductInputPort;
import org.sotil.kuarkus.demo.application.port.out.ProductOutputPort;
import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductUseCase implements ProductInputPort {

  @Inject
  ProductOutputPort productOutputPort;

  @Override
  public List<Product> list() {
    return productOutputPort.findAll();
  }

}
