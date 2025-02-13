package org.sotil.kuarkus.demo.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.sotil.kuarkus.demo.application.ports.in.CustomerVertxClientServicePort;
import org.sotil.kuarkus.demo.application.ports.out.ProductApiClientStorkPort;
import org.sotil.kuarkus.demo.domain.models.Customer;
import org.sotil.kuarkus.demo.domain.models.Product;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerVertxClientUseCaseImpl implements CustomerVertxClientServicePort {

  @RestClient
  ProductApiClientStorkPort productApiClientStorkPort;

  @ConfigProperty(name = "kuarkus.demo.application.product.host")
  String productHost;
  @ConfigProperty(name = "kuarkus.demo.application.product.port")
  Integer productPort;
  @ConfigProperty(name = "kuarkus.demo.application.product.path")
  String productPath;

  @Inject
  Vertx vertx;

  private WebClient webClient;

  @PostConstruct
  void initialize() {
    this.webClient = WebClient.create(vertx,
      new WebClientOptions().setDefaultHost(productHost)
        .setDefaultPort(productPort).setSsl(false).setTrustAll(true));
  }

  @WithSession
  @Override
  public Uni<Customer> getByIdProduct(Long Id) {
    return Uni.combine().all().unis(getCustomerReactive(Id), getAllProducts())
      .combinedWith((customerDb, productsClient) -> {
        customerDb.getProducts().forEach(productDb -> {
          productsClient.forEach(productClient -> {
            log.info("Ids are: " + productDb.getProduct() + " = " + productClient.getId());
            if (productDb.getProduct() == productClient.getId()) {
              productDb.setName(productClient.getName());
              productDb.setDescription(productClient.getDescription());
              ;
            }
          });
        });
        return customerDb;
      });
  }

  private Uni<Customer> getCustomerReactive(Long Id) {
    log.info("get value using active record {}", Id);
    return Customer.findById(Id);
  }


  private Uni<List<Product>> getAllProducts() {
    return webClient.get(productPort, productHost, productPath).send()
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

