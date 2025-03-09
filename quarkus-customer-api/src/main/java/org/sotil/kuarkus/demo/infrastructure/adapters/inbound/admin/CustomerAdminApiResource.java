package org.sotil.kuarkus.demo.infrastructure.adapters.inbound.admin;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestPath;
import org.sotil.kuarkus.demo.application.ports.in.CustomerCrudServicePort;
import org.sotil.kuarkus.demo.domain.models.Customer;

import static jakarta.ws.rs.core.Response.Status.*;

@Slf4j
@Path("/admin/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerAdminApiResource {

  @Inject
  CustomerCrudServicePort customerServicePort;

  @POST
  @Path("/add")
  public Uni<Response> add(Customer c) {
    log.info("Saving Customer ::{}", c);
    return customerServicePort.add(c)
      .replaceWith(Response.ok(c).status(CREATED)::build);
  }

  @DELETE
  @Path("/delete/{id}")
  public Uni<Response> delete(@PathParam("Id") Long id) {
    log.info("deleting by id :: {}", id);
    return customerServicePort.delete(id)
      .map(deleted -> deleted
        ? Response.ok().status(NO_CONTENT).build()
        : Response.ok().status(NOT_FOUND).build());
  }

  @PUT
  @Path("/update/{id}")
  public Uni<Response> update(@RestPath Long id, Customer c) {
    log.info("Updating customer id ::{}", id);
    return customerServicePort.update(id, c)
      .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
      .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
  }

}
