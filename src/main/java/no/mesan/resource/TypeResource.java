package no.mesan.resource;

import no.mesan.service.TypeService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static no.mesan.resource.RestUrlPattern.API;

@Path(API + "/type")
@Produces(APPLICATION_JSON)
public class TypeResource {
    private TypeService typeService;

    public TypeResource(TypeService typeService) {
        this.typeService = typeService;
    }

    @GET
    @RolesAllowed("USER")
    public Response getTypes() {
        return Response.ok(typeService.getTypes()).build();
    }
}
