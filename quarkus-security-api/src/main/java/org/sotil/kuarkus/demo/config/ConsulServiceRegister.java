package org.sotil.kuarkus.demo.config;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ConsulServiceRegister {

    private static final Logger log = LoggerFactory.getLogger(ConsulServiceRegister.class);

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
                log.info("Successfully registered service '" + appName + "' with Consul");
            } else {
                log.error("Failed to register service " + appName + " with Consul: " + res.cause().getMessage());
            }
        });
    }

    public void shutdown(@Observes ShutdownEvent ev) {
        if (consulClient != null) {
            consulClient.deregisterService(serviceId, res -> {
                if (res.succeeded()) {
                    log.info("Successfully deregistered service '" + serviceId + "' from Consul");
                } else {
                    log.error("Failed to deregister service '" + serviceId + "': " + res.cause().getMessage());
                }
            });
        }
    }

}
