package info.rori.lunchbox.api.v1.resource;

//import io.swagger.annotations.*;
import info.rori.lunchbox.api.v1.model.LunchOffer;
import info.rori.lunchbox.api.v1.repository.LunchOfferRepository;
import info.rori.lunchbox.api.v1.util.TypeConverter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

/**
 * Stellt die Mittagsangebote in der REST-API bereit.
 */
@OpenAPIDefinition(
	    info = @Info(
	            title = "Lunchbox API v1",
	            version = "1.2.0",
	            description = "API für die Abfrage von Mittagsangeboten"
	    ),
	    servers = @Server(url = "http://localhost:8080/api/v1")
	)
@Path("/lunchOffer")
@Produces(MediaType.APPLICATION_JSON)
public class LunchOfferResource {

    private LunchOfferRepository repo = new LunchOfferRepository();

    @GET
    @Operation(summary = "Liefert alle Mittagsangebote, ggf. gefiltert nach Gültigkeits-Tag",
               responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                        array = @ArraySchema( schema = @Schema(implementation = LunchOffer.class)
                    ))),
                    @ApiResponse(responseCode = "400", description = "Bad Request - der Parameter 'day' ist nicht valide"),
                    @ApiResponse(responseCode = "500", description = "Serverfehler")
               })
    public List<LunchOffer> get(
            @QueryParam("day")
            @Parameter(description = "Tag, an dem die Mittagsangebote gültig sein sollen. Format: 'YYYY-MM-DD' (ISO 8601)", required = false)
            String dayString) {

        LocalDate day = null;
        if (dayString != null) {
            day = TypeConverter.toDate(dayString);
            if (day == null)
                throw new BadRequestException("'day' ist nicht valide");
        }

        if (day == null)
            return repo.findAll();
        else
            return repo.findByDay(day);
    }

    @GET
    @Path("{id : \\d+}")
    @Operation(summary = "Liefert das Mittagsangebot mit der angegebenen ID",
               responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                        schema = @Schema(implementation = LunchOffer.class)
                    )),
                    @ApiResponse(responseCode = "404", description = "Unter der angegebenen ID existiert kein Mittagsangebot"),
                    @ApiResponse(responseCode = "500", description = "Serverfehler")
               })
    public LunchOffer getById(
            @PathParam("id")
            @Parameter(description = "ID des gesuchten Mittagsangebots", required = true)
            int id) {
        return repo.findById(id);
    }

}
