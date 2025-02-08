package org.sotil.kuarkus.demo.application.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.sotil.kuarkus.demo.application.ports.in.CustomerServicePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.sotil.kuarkus.demo.application.ports.out.CustomerOutputPort;
import org.sotil.kuarkus.demo.domain.models.Customer;
import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerService implements CustomerServicePort {

    @Inject
    CustomerOutputPort customerOutputPort;

    @Inject
    Vertx vertx;

    private WebClient webClient;

    @PostConstruct
    void initialize() {
        this.webClient = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("localhost")
                        .setDefaultPort(8081).setSsl(false).setTrustAll(true));
    }

    @WithSession
    @Override
    public Uni<List<Customer>> listCustomers() {
        //return Customer.listAll(Sort.by("names"));
        log.info("find listCustomers");
        customerOutputPort.findTestRepo();
        return customerOutputPort.listAll();
    }

    @WithSessionOnDemand
    @Override
    public Uni<Customer> getById(Long Id) {
        return Customer.findById(Id);
    }

    @WithTransaction
    @Override
    public Uni<Customer> add(Customer c) {
        c.getProducts().forEach(p -> p.setCustomer(c));
        return Panache.withTransaction(c::persist)
                .replaceWith(c);
    }

    @WithTransaction
    @Override
    public Uni<Boolean> delete(Long Id) {
        return Customer.deleteById(Id);
    }

    @WithTransaction
    @Override
    public Uni<Customer> update(Long id, Customer c) {
        return Panache
                .withTransaction(() -> Customer.<Customer>findById(id)
                        .onItem().ifNotNull().invoke(entity -> {
                            entity.setNames(c.getNames());
                            entity.setAccountNumber(c.getAccountNumber());
                            entity.setCode(c.getCode());
                        })
                )
                .replaceWith(c);
    }

    @WithSessionOnDemand
    @Override
    public Uni<List<Customer>> listUsingRepository() {
        return customerOutputPort.findAll().list();
    }

    @WithSession
    @Override
    public Uni<Customer> getByIdProduct(Long Id) {
        return Uni.combine().all().unis(getCustomerReactive(Id), getAllProducts())
                .combinedWith((v1, v2) -> {
                    v1.getProducts().forEach(product -> {
                        v2.forEach(p -> {
                            log.info("Ids are: " + product.getProduct() + " = " + p.getId());
                            if (product.getProduct() == p.getId()) {
                                product.setName(p.getName());
                                product.setDescription(p.getDescription());
                                ;
                            }
                        });
                    });
                    return v1;
                });
    }

    private Uni<Customer> getCustomerReactive(Long Id) {
        return Customer.findById(Id);
    }

    private Uni<List<Product>> getAllProducts() {
        return webClient.get(8081, "localhost", "/product").send()
                .onFailure().invoke(res -> log.error("Error recuperando productos ", res))
                .onItem().transform(res -> {
                    List<Product> lista = new ArrayList<>();
                    JsonArray objects = res.bodyAsJsonArray();
                    objects.forEach(p -> {
                        log.info("See Objects: " + objects);
                        ObjectMapper objectMapper = new ObjectMapper();
                        // Pass JSON string and the POJO class
                        Product product = null;
                        try {
                            product = objectMapper.readValue(p.toString(), Product.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        lista.add(product);
                    });
                    return lista;
                });
    }


}

