package info.rori.lunchbox.api.v1.resource;

import io.swagger.annotations.*;
import info.rori.lunchbox.api.v1.model.LunchOffer;
import info.rori.lunchbox.api.v1.repository.LunchOfferRepository;
import info.rori.lunchbox.api.v1.util.TypeConverter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

/**
 * Stellt die Mittagsangebote in der REST-API bereit.
 */
@Path("/lunchOffer")
@Api(value = "/lunchOffer", description = "Liefert die Mittagsangebote")
public class LunchOfferResource {

    private LunchOfferRepository repo = new LunchOfferRepository();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Liefert alle Mittagsangebote, ggf. gefiltert nach Gültigkeits-Tag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = LunchOffer.class),
            @ApiResponse(code = 400, message = "Bad Request - der Parameter 'day' ist nicht valide"),
            @ApiResponse(code = 500, message = "Serverfehler")})
    public List<LunchOffer> get(
            @QueryParam("day")
            @ApiParam(value = "Tag, an dem die Mittagsangebote gültig sein sollen. Format: 'YYYY-MM-DD' (ISO 8601)", required = false)
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
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Liefert das Mittagsangebot mit der angegebenen ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = LunchOffer.class),
            @ApiResponse(code = 404, message = "Unter der angegebenen ID existiert kein Mittagsangebot"),
            @ApiResponse(code = 500, message = "Serverfehler")})
    public LunchOffer getById(
            @PathParam("id")
            @ApiParam(value = "ID des gesuchten Mittagsangebots", required = true)
            int id) {
        return repo.findById(id);
    }

}
