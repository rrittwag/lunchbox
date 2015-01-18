package info.rori.lunchbox.api.v1.resource;

import com.wordnik.swagger.annotations.*;
import info.rori.lunchbox.api.v1.model.LunchProvider;
import info.rori.lunchbox.api.v1.repository.LunchProviderRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Stellt die Mittagsanbieter in der REST-API bereit.
 */
@Path("/lunchProvider")
@Api(value = "/lunchProvider", description = "Liefert die Mittagsanbieter")
public class LunchProviderResource {

    private LunchProviderRepository repo = new LunchProviderRepository();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Liefert alle Mittagsanbieter")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Serverfehler")})
    public List<LunchProvider> get() {
        return repo.findAll();
    }

    @GET
    @Path("{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Liefert den Mittagsanbieter mit der angegebenen ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Unter der angegebenen ID existiert kein Mittagsanbieter"),
            @ApiResponse(code = 500, message = "Serverfehler")})
    public LunchProvider getById(
            @PathParam("id")
            @ApiParam(value = "ID des gesuchten Mittagsanbieters", required = true)
            String id) {
        return repo.findById(Integer.parseInt(id));
    }

}