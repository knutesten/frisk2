package no.mesan.resource;

import no.mesan.service.LeaderboardService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/leaderboard")
@Produces(APPLICATION_JSON)
public class LeaderboardResource {
    private LeaderboardService leaderboardService;

    public LeaderboardResource(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GET
    @RolesAllowed("USER")
    public Response getLeaderboard() {
        return Response.ok(leaderboardService.getLeaderboard()).build();
    }
}
