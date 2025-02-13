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

  @ConfigProperty(name = "quarkus.http.port", defaultValue = "8083")
  int appPort;
  @ConfigProperty(name = "quarkus.application.name", defaultValue = "ms-quarkus-productb-api")
  String appName;

  public void init(@Observes StartupEvent ev, Vertx vertx) {
    ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(consulHost).setPort(consulPort));
    client.registerService(new ServiceOptions().setPort(appPort).setAddress(consulHost).setName(appName).setId(appName + '-' + appPort));
    log.info("Register api to discovery with name {}", appName);
  }

}
