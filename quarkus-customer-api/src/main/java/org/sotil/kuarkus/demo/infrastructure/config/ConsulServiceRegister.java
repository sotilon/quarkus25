package org.sotil.kuarkus.demo.infrastructure.config;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class ConsulServiceRegister {

  @ConfigProperty(name = "consul.host", defaultValue = "localhost")
  String consulHost;
  @ConfigProperty(name = "consul.port", defaultValue = "8500")
  int consulPort;

  @ConfigProperty(name = "quarkus.application.name", defaultValue = "ms-quarkus-customer-api")
  String appName;
  @ConfigProperty(name = "quarkus.http.port", defaultValue = "8082")
  int appPort;

    /*
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(consulHost).setPort(consulPort));
        client.registerService(new ServiceOptions().setPort(appPort).setAddress(consulHost).setName(appName).setId(appName +'-'+ appPort));
        log.info("Customer API registration to consul Service name:: {}", appName);
        log.info("Customer API registration to consul Service id:: {}", appName +'-'+ consulPort);
    }
*/

  public void init(@Observes StartupEvent ev, Vertx vertx) {
    String serviceNameId = appName.concat("-") + appPort;
    // Configuración del cliente Consul
    ConsulClientOptions options = new ConsulClientOptions()
      .setHost(consulHost)  // Dirección del servidor Consul
      .setPort(consulPort)         // Puerto por defecto de Consul
      ;     // Tiempo de espera en milisegundos
    ConsulClient consulClient = ConsulClient.create(vertx, options);
    // Configurar el servicio a registrar
    ServiceOptions serviceOptions = new ServiceOptions()
      .setPort(appPort)
      .setAddress(consulHost)
      .setName(appName)
      .setId(serviceNameId);

    // Registrar el servicio en Consul
    consulClient.registerService(serviceOptions);
        /*consulClient.healthState(HealthState.ANY, res -> {
            if (res.succeeded()) {
                res.result().getList().forEach(check -> {
                    System.out.println("Servicio: " + check.getServiceName());
                    System.out.println("Estado: " + check.getStatus());
                });
            } else {
                System.err.println("Error obteniendo estado de servicios: " + res.cause().getMessage());
            }
        }); */
  }

}
