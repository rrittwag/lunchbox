package info.rori.lunchbox.api.v1.resource;

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
public class LunchOfferResource {

    private LunchOfferRepository repo = new LunchOfferRepository();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LunchOffer> get(@QueryParam("day") String dayString) {
        LocalDate day = TypeConverter.toDate(dayString);
        if (day == null)
            return repo.findAll();
        else
            return repo.findByDay(day);
    }

    @GET
    @Path("{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public LunchOffer getById(@PathParam("id") String id) {
        return repo.findById(Integer.parseInt(id));
    }

}
