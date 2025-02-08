package org.sotil.kuarkus.demo.application.ports.out;

import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.List;

public interface ProductGraphQLClientPort {

    List<Product> getProductsGrapQl() throws Exception ;

    Product getByIdProductGrapQl(Long Id) throws Exception ;
}
