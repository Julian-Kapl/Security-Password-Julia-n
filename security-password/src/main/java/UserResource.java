import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.Arrays;

@Path("/api/v1/users")
@Slf4j
public class UserResource {
    @Inject
    private UserService service;
    @GET
    public Response getAllUsers() {
        return Response.ok(service.getAllUsers()).build();
    }

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") final String username) {
        log.info("user {}", username);

        final User user = service.getUser(username);
        if(user != null) {
            return Response.ok(user).build();
        }
        return Response.noContent().build();
    }
}