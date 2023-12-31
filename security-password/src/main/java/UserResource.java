import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.net.URI;
import java.net.URISyntaxException;
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
        log.info("Username: ", username);

        try{
            final User user = service.getUser(username);
            if(user != null) {
                return Response.ok(user).build();
            }
            return Response.noContent().build();

        } catch(final Exception ex){
            log.warn(ex.toString(),ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    @POST
    public Response createUser(final User user) throws URISyntaxException {
        log.info("new user {}", user.toString());

        try{
            service.saveUser(user);
            log.info("createdUser - created {}", user.getUsername());
            return Response.created(new URI("/api/v1/users/" + user.getUsername())).build();

        }catch (final Exception ex){
            log.warn(ex.toString(),ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    @PUT
    @Path("/{username}")
    public Response changePassword(@PathParam("username") final String username,final User newUser) throws URISyntaxException{
        log.info("user {}", username);
        log.info("new password", newUser.toString());
        try{
            User user = service.getUser(username);
            log.info("Datenbankuser:" ,user.toString());
            user.setPassword(newUser.getPassword());
            service.changePassword(user, newUser.getPassword());
            return Response.ok().build();

        }catch (final Exception ex){
            log.warn(ex.toString(),ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}