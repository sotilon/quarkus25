package org.sotil.kuarkus.demo.application.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.sotil.kuarkus.demo.application.ports.in.CustomerStorkFallClientServicePort;
import org.sotil.kuarkus.demo.application.ports.out.CustomerRepoOutputPort;
import org.sotil.kuarkus.demo.application.ports.out.ProductApiClientStorkPort;
import org.sotil.kuarkus.demo.application.ports.out.ProductBApiClientStorkFallBackPort;
import org.sotil.kuarkus.demo.domain.models.Customer;
import org.sotil.kuarkus.demo.infrastructure.dto.ProductData;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerStorkFallBackClientUseCaseImpl implements CustomerStorkFallClientServicePort {

  @Inject
  CustomerRepoOutputPort customerOutputPort;

  @RestClient
  ProductApiClientStorkPort productApiClientStorkPort;

  @RestClient
  ProductBApiClientStorkFallBackPort productBApiClientStorkFallBankPort;

  @WithSession
  @Override
  public Uni<Customer> listUsingRepository(Long id) {
    Uni<Customer> customerOptional = customerOutputPort.findById(id);
    return customerOptional.onItem().ifNotNull().transformToUni(customer -> {
      customer.getProducts().forEach(productDB -> {
        log.info("Getting Product data from client by product id :: {}", productDB.getProduct());
        Uni<ProductData> productData = productApiClientStorkPort.findProductById(productDB.getProduct());
        productData.subscribe().with(pd -> {
          log.info("Get data from client product name ::{}", pd.name());
          productDB.setCode(pd.code());
          productDB.setName(pd.name());
          productDB.setDescription(pd.description());
        }, faillure -> {
          System.out.println("erorrrrrrr");
          System.out.println(faillure.getMessage());
        });

      });
      return customer.persistAndFlush();
    });
  }


  private Uni<Customer> getCustomerReactive(Long Id) {
    return Customer.findById(Id);
  }

  @WithSession
  @Override
  public Uni<Customer> findCustomerWithProductFill(Long customerId) {
    log.info("::findCustomerWithProductFill:: {}", customerId);
    return Uni.combine().all().unis(getCustomerUsingRepository(customerId), getAllProductsFromClient()).combinedWith((customerDb, productsClient) -> {
      customerDb.getProducts().forEach(productDb -> {
        productsClient.forEach(productClient -> {
          log.info("Ids are: " + productDb.getProduct() + " = " + productClient.id());
          if (productDb.getProduct() == productClient.id()) {
            productDb.setId(productClient.id());
            productDb.setCode(productClient.code());
            productDb.setName(productClient.name());
            productDb.setDescription(productClient.description());
          }
        });
      });
      return customerDb;
    });
  }

  private Uni<Customer> getCustomerUsingRepository(Long id) {
    log.info("get customer by id :: {} using repository", id);
    return customerOutputPort.findById(id);
  }

  /*+
  @Retry(maxRetries = 3, delay = 500)
  Reintenta la operación hasta 3 veces con un retraso de 500ms entre intentos.

  @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 2000)
  Si más del 50% de las solicitudes fallan en un volumen de 4 peticiones, el circuito se abre y bloquea nuevas solicitudes por 2 segundos.

  @Timeout(1000)
  Si la operación tarda más de 1 segundo, se cancela y se lanza un error.

  @Fallback(fallbackMethod = "fallbackResponse")
  Si todas las estrategias anteriores fallan, se ejecuta el metodo fallbackResponse() para devolver una respuesta alternativa.
   */
  @Retry(maxRetries = 3, delay = 500)
  @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 2000)
  @Timeout(1000)
  @Fallback(fallbackMethod = "fallbackResponse")
  public Uni<List<ProductData>> getAllProductsFromClient() {
    log.info("get all product by stork ");
    Uni<List<ProductData>> productsData = productApiClientStorkPort.getAllProducts();
    return productsData.invoke(productsData1 -> {
      productsData1.forEach(productData -> {
        log.info(productData.name());
      });
    }).onFailure(WebApplicationException.class).recoverWithItem(ex -> {
      log.error("Error al obtener productos: " + ex.getMessage());
      return List.of(); // Retornar una lista vacía en caso de error
    });
  }

  public Uni<List<ProductData>> fallbackResponse() {
    log.info("get all product by stork fallbackResponse ");
    Uni<List<ProductData>> productsData = productBApiClientStorkFallBankPort.getAllProducts();
    return productsData.invoke(productsData1 -> {
      productsData1.forEach(productData -> {
        log.info(productData.name());
      });
    }).onFailure(WebApplicationException.class).recoverWithItem(ex -> {
      log.error("Error al obtener productos: " + ex.getMessage());
      return List.of(); // Retornar una lista vacía en caso de error
    });
  }

}

