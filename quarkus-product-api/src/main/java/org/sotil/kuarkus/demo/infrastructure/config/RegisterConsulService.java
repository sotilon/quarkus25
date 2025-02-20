package org.sotil.kuarkus.demo.infrastructure.config;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class RegisterConsulService {

  @ConfigProperty(name = "consul.host", defaultValue = "localhost")
  String consulHost;
  @ConfigProperty(name = "consul.port", defaultValue = "8500")
  int consulPort;

  @ConfigProperty(name = "app.service.address", defaultValue = "localhost")
  String serviceAddress;
  @ConfigProperty(name = "quarkus.http.port", defaultValue = "8081")
  int appPort;
  @ConfigProperty(name = "quarkus.application.name", defaultValue = "ms-quarkus-product-api")
  String appName;

  private ConsulClient consulClient;
  private String serviceId;

  @Inject
  Vertx vertx;

  public void init(@Observes StartupEvent ev) {
    serviceId = appName + "-" + appPort;
    consulClient = ConsulClient.create(vertx, new ConsulClientOptions()
      .setHost(consulHost)
      .setPort(consulPort));
    ServiceOptions serviceOptions = new ServiceOptions()
      .setId(serviceId)
      .setName(appName)
      .setAddress(serviceAddress)  // Use actual service address
      .setPort(appPort);

    consulClient.registerService(serviceOptions, res -> {
      if (res.succeeded()) {
        log.info("✅ Successfully registered service '%s' with Consul at %s:%d", appName, consulHost, consulPort);
      } else {
        log.error("❌ Failed to register service '%s' with Consul: %s", appName, res.cause().getMessage());
      }
    });

    log.info(":: Register info ::");
    log.info("consul uri ::{} , consult port :: {}", consulHost, consulPort);
    log.info("app serviceAddress ::{} , app name::{}, app port :: {}", serviceAddress, appName, appPort);
  }

  public void shutdown(@Observes ShutdownEvent ev) {
    if (consulClient != null) {
      consulClient.deregisterService(serviceId, res -> {
        if (res.succeeded()) {
          log.info("✅ Successfully deregistered service '%s' from Consul", serviceId);
        } else {
          log.error("❌ Failed to deregister service '%s': %s", serviceId, res.cause().getMessage());
        }
      });
    }
  }

}
