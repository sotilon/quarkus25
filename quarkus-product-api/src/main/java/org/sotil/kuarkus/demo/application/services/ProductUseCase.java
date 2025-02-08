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

    @Override
    public Optional<Product> getById(Long id) {
        return productOutputPort.findById(id);
    }

    @Override
    public Product add(Product p) {
        return productOutputPort.save(p);
    }

    @Override
    public Boolean delete(Long id) {
        Product p = productOutputPort.getById(id);
        if(p==null){
            return false;
        }
        productOutputPort.delete(p);
        return true;
    }

    @Override
    public Product update(Product p) {
        return productOutputPort.save(p);
    }
}
