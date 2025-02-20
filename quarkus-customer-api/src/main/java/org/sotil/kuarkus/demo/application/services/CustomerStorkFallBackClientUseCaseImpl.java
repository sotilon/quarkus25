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
import org.sotil.kuarkus.demo.application.ports.dtos.TokenResponse;
import org.sotil.kuarkus.demo.application.ports.dtos.UserDTO;
import org.sotil.kuarkus.demo.application.ports.in.CustomerStorkFallClientServicePort;
import org.sotil.kuarkus.demo.application.ports.out.CustomerRepoOutputPort;
import org.sotil.kuarkus.demo.application.ports.out.ProductApiClientStorkPort;
import org.sotil.kuarkus.demo.application.ports.out.ProductBackUpApiClientStorkFallBackPort;
import org.sotil.kuarkus.demo.application.ports.out.SecurityApiClientStorkPort;
import org.sotil.kuarkus.demo.domain.models.Customer;
import org.sotil.kuarkus.demo.infrastructure.dto.ProductData;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * El patrón sigue tres estados principales:
 *
 * 🔵 CLOSED (Cerrado - Normal)
 *
 * Todas las solicitudes pasan normalmente.
 * Si un número determinado de fallos ocurre en un período corto, el circuito se abre.
 * 🔴 OPEN (Abierto - Bloqueo)
 *
 * Se bloquean todas las solicitudes durante un tiempo determinado.
 * Esto evita que el sistema siga intentando llamadas a un servicio que está fallando.
 * 🟡 HALF-OPEN (Medio Abierto - Prueba)
 *
 * Después del tiempo de espera, el circuito permite algunas solicitudes de prueba.
 * Si las solicitudes tienen éxito, el circuito vuelve a CLOSED.
 * Si fallan, el circuito se mantiene en OPEN por más tiempo.
 * ✅ Evita fallos en cascada: Protege el sistema de sobrecarga cuando un servicio externo falla.
 * ✅ Mejora la resiliencia: Permite que el sistema se recupere sin colapsar.
 * ✅ Optimiza el rendimiento: Reduce intentos innecesarios en servicios no disponibles.
 * ✅ Mejor experiencia de usuario: En lugar de errores, se devuelve una respuesta de fallback.
 */

/**
 * Stork es un servicio de descubrimiento y balanceo de carga para microservicios en Quarkus.
 *  Su objetivo es facilitar la comunicación entre servicios en entornos distribuidos,
 *  como Kubernetes o arquitecturas de microservicios, sin depender de herramientas externas como Consul, Eureka o Istio.
 *  Stork resuelve este problema proporcionando:
 *  ✅ Descubrimiento de servicios: Encuentra instancias de servicios dinámicamente.
 *  ✅ Balanceo de carga: Distribuye las solicitudes entre múltiples instancias.
 *  ✅ Soporte para Kubernetes: Puede descubrir servicios en entornos de Kubernetes.
 */
@Slf4j
@ApplicationScoped
public class CustomerStorkFallBackClientUseCaseImpl implements CustomerStorkFallClientServicePort {

  @Inject
  CustomerRepoOutputPort customerOutputPort;

  @RestClient
  ProductApiClientStorkPort productApiClientStorkPort;

  @RestClient
  ProductBackUpApiClientStorkFallBackPort productBApiClientStorkFallBankPort;

  @RestClient
  SecurityApiClientStorkPort securityApiClientStorkPort;


  @WithSession
  @Override
  public Uni<Customer> listUsingRepository(Long id) {
    log.info("::FIND USING STORK CLIENT::IT is FAILED");
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
          System.out.println("Error");
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
    log.info("::FIND USING STORK CLIENT::IT is OK");
    log.info("::findCustomerWithProductFill:: {}", customerId);
    return Uni.combine().all()
      .unis(getCustomerUsingRepository(customerId), getAllProductsFromClient())
      .asTuple()
      .map(tuple -> {
        Customer customerDb = tuple.getItem1();
        List<ProductData> productsClient = tuple.getItem2();
        customerDb.getProducts().forEach(productDb -> {
          productsClient.forEach(productClient -> {
            log.info("Ids are: " + productDb.getProduct() + " = " + productClient.id());
            if (productDb.getProduct().equals(productClient.id())) {
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
  Si más del 50% de las solicitudes fallan en un volumen de 4 peticiones,
   el circuito se abre y bloquea nuevas solicitudes por 2 segundos.

  @Timeout(1000)
  Si la operación tarda más de 1 segundo, se cancela y se lanza un error.

  @Fallback(fallbackMethod = "fallbackResponse")
  Si todas las estrategias anteriores fallan,
  se ejecuta el metodo fallbackResponse() para devolver una respuesta alternativa.
   */
  @Retry(maxRetries = 3, delay = 500)
  @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 2000)
  @Timeout(1000)
  @Fallback(fallbackMethod = "fallbackResponse")
  public Uni<List<ProductData>> getAllProductsFromClient() {
    log.info("::get all product by stork well or fallback ");
    Uni<List<ProductData>> productsData = productApiClientStorkPort.getAllProducts();
    return productsData.invoke(productsData1 -> {
      productsData1.forEach(productData -> {
        log.info("::product name by stork ok :: {}",productData.name());
      });
    }).onFailure(WebApplicationException.class).recoverWithItem(ex -> {
      log.error("::Error getting productos absolute: " + ex.getMessage());
      return List.of(); // Return an empty list in case of error by it  is not fallback response
    });
  }

  public Uni<List<ProductData>> fallbackResponse() {
    log.info("::get all product by stork fallbackResponse");
    return getJwtTokenFromJson()
      .flatMap(token -> {
        String bearerToken = "Bearer " + token;
        return productBApiClientStorkFallBankPort.getAllProducts(bearerToken);
      })
      .invoke(productsData -> {
        productsData.forEach(productData -> log.info("product name by stork fallback :: {}",productData.name()));
      })
      .onFailure(WebApplicationException.class)
      .recoverWithItem(ex -> {
        log.error("::Error getting productos fallbackResponse: " + ex.getMessage());
        return List.of(); // Return an empty list in case of error
      });
  }

  private Uni<String> getJwtTokenFromJson() {
    return securityApiClientStorkPort.getTokeJwt(new UserDTO("VENDOR", "user123"))
      .onItem().transform(response -> response.readEntity(TokenResponse.class)) // Convert to DTO
      .onItem().transform(TokenResponse::getToken); // Extract token field
  }

}

