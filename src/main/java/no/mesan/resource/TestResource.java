package no.mesan.resource;

import io.dropwizard.auth.Auth;
import no.mesan.dao.UserDao;
import no.mesan.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/test")
@Produces("application/json")
public class TestResource {
    private UserDao userDao;

    public TestResource(UserDao userDao) {
        this.userDao = userDao;
    }

    @GET
    public User test(@Auth User user) {
        return user;
    }
}
