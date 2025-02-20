package org.sotil.kuarkus.demo.resource;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.sotil.kuarkus.demo.domain.User;
import org.sotil.kuarkus.demo.domain.UserDTO;
import org.sotil.kuarkus.demo.repository.UserRepository;
import org.sotil.kuarkus.demo.service.JwtService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Inject
    UserRepository userRepository;

    @Inject
    JwtService jwtService;

    @GET
    @Path("/users")
    public List<UserDTO> list() {
        log.info("Getting all users");
        return userRepository.listAll().stream()
                .map(user -> new UserDTO(user.username, user.roles,"")) // Transformamos los datos
                .collect(Collectors.toList()).reversed();
    }

    @GET
    @Path("/user/{userName}")
    public UserDTO list(@PathParam("userName") String userName) {
        log.info("Getting User by user name "+userName);
        User existingUser = userRepository.findByUsername(userName);
        return new UserDTO(existingUser.username,existingUser.roles,"");
    }

    @POST
    @Path("/admin/login")
    public Response login(UserDTO user) {
        log.info("User:::"+user.username().toString());
        User existingUser = userRepository.findByUsername(user.username());
        //if (existingUser != null && existingUser.password.equals(user.password)) {
        if (existingUser != null && BcryptUtil.matches(user.password(),existingUser.password)) {
            String token = jwtService.generateToken(existingUser.username, existingUser.roles);
            return Response.ok(new TokenResponse(token)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public static class TokenResponse {
        public String token;
        public TokenResponse(String token) {
            this.token = token;
        }
    }
}
