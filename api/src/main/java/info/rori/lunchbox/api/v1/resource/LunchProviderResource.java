package info.rori.lunchbox.api.v1.resource;

import info.rori.lunchbox.api.v1.model.LunchProvider;
import info.rori.lunchbox.api.v1.repository.LunchProviderRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Stellt die Mittagsanbieter in der REST-API bereit.
 */
@Path("/lunchProvider")
public class LunchProviderResource {

    private LunchProviderRepository repo = new LunchProviderRepository();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LunchProvider> get(@QueryParam("location") String location) {
        if (location == null)
            return repo.findAll();
        else
            return repo.findByLocation(location);
    }

    @GET
    @Path("{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public LunchProvider getById(@PathParam("id") String id) {
        return repo.findById(Integer.parseInt(id));
    }

}