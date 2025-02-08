package org.sotil.kuarkus.demo.application.services;

import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.sotil.kuarkus.demo.application.ports.out.ProductGraphQLClientPort;
import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.List;

import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Argument.args;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

@ApplicationScoped
public class CustomerGraphQLService implements ProductGraphQLClientPort {

    @Inject
    @GraphQLClient("product-dynamic-client")
    DynamicGraphQLClient dynamicGraphQLClient;


    public List<Product> getProductsGrapQl() throws Exception {
        Document query = document(
                operation(
                        field("allProducts",
                                field("id"),
                                field("code"),
                                field("name"),
                                field("description")
                        )
                )
        );
        io.smallrye.graphql.client.Response response = dynamicGraphQLClient.executeSync(query);
        return response.getList(Product.class,"allProducts");
    }

    public Product getByIdProductGrapQl(Long Id) throws Exception {
        Document query = document(
                operation(
                        field("product",
                                args(arg("productId", Id)),
                                field("id"),
                                field("code"),
                                field("name"),
                                field("description")
                        )
                )
        );
        io.smallrye.graphql.client.Response response = dynamicGraphQLClient.executeSync(query);
        return response.getObject(Product.class,"product");
    }
}
