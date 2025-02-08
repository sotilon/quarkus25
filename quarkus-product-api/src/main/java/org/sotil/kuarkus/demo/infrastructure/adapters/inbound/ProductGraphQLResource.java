package org.sotil.kuarkus.demo.infrastructure.adapters.inbound;

import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.*;
import org.sotil.kuarkus.demo.application.port.in.ProductInputPort;
import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.List;
import java.util.Optional;

@GraphQLApi
public class ProductGraphQLResource {

    @Inject
    ProductInputPort productInputPort;

    @Query("allProducts")
    @Description("Get all products from a database")
    public List<Product> getAllProducts(){
        return  productInputPort.list();
    }

    @Query("product")
    @Description("Get a product from database")
    public Optional<Product> getProduct(@Name("productId") Long id) {
        return productInputPort.getById(id);
    }


    //Optional methods that not was requiered on Chanlenge
    @Mutation
    @Description("Add a new product to database")
    public Product addProduct(@Name("product") Product product) {
        return productInputPort.add(product);
    }

    @Mutation
    @Description("Update a product in database")
    public Product updateProduct(@Name("product") Product product) {
        return productInputPort.update(product);
    }

    @Mutation
    @Description("Delete a product by ID")
    public boolean deleteProduct(@Name("productId") Long productId) {
        try {
            productInputPort.delete(productId);
            return true; // La eliminación tuvo éxito
        } catch (Exception e) {
            return false; // La eliminación falló
        }
    }


}
