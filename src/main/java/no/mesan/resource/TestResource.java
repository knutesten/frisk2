package no.mesan.resource;

import no.mesan.dao.UserDao;
import no.mesan.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/test")
@Produces("application/json")
public class TestResource {
    public UserDao userDao;

    public TestResource(UserDao userDao) {
        this.userDao = userDao;
    }

    @GET
    public User test() {
        return userDao.getUserByEmail("knut.neksa@gmail.com");
    }
}
