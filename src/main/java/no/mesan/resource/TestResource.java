package no.mesan.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by knutn on 7/19/2016.
 */
@Path("/test")
public class TestResource {
    @GET
    public String test() {
        return "Dette er en hyperfarlig test";
    }
}
