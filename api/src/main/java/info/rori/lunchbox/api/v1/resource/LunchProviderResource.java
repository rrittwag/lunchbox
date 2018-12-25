package info.rori.lunchbox.api.v1.resource;

import info.rori.lunchbox.api.v1.model.LunchProvider;
import info.rori.lunchbox.api.v1.repository.LunchProviderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Stellt die Mittagsanbieter in der REST-API bereit.
 */
@Path("/lunchProvider")
@Produces(MediaType.APPLICATION_JSON)
public class LunchProviderResource {

    private LunchProviderRepository repo = new LunchProviderRepository();

    @GET
    @Operation(summary = "Liefert alle Mittagsanbieter",
               responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                        array = @ArraySchema( schema = @Schema(implementation = LunchProvider.class)
                    ))),
                    @ApiResponse(responseCode = "500", description = "Serverfehler")
               })
    public List<LunchProvider> get() {
        return repo.findAll();
    }

    @GET
    @Path("{id : \\d+}")
    @Operation(summary = "Liefert den Mittagsanbieter mit der angegebenen ID",
               responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                        schema = @Schema(implementation = LunchProvider.class)
                    )),
                    @ApiResponse(responseCode = "404", description = "Unter der angegebenen ID existiert kein Mittagsanbieter"),
                    @ApiResponse(responseCode = "500", description = "Serverfehler")
               })
    public LunchProvider getById(
            @PathParam("id")
            @Parameter(description = "ID des gesuchten Mittagsanbieters", required = true)
            int id) {
        return repo.findById(id);
    }

}
