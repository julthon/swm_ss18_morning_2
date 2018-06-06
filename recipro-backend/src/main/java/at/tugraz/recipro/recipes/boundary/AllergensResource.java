package at.tugraz.recipro.recipes.boundary;

import at.tugraz.recipro.recipes.control.AllergensManager;
import at.tugraz.recipro.recipes.entity.Allergen;
import io.swagger.annotations.Api;
import java.net.URI;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Julian
 */
@Path("allergens")
@Stateless
@Api(value = "AllergensResource")
public class AllergensResource {   
    @Inject
    AllergensManager allergensManager;
    
    @POST
    public Response create(Allergen allergen, @Context UriInfo uriInfo) {
        Allergen savedAllergen = allergensManager.save(allergen);
        String shortName = savedAllergen.getShortName();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + shortName).build();
        return Response.created(uri).build();
    }
    
    @GET
    @Path("{shortName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Allergen findByShortName(@PathParam("shortName") String shortName) {
        return allergensManager.findByShortName(shortName);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Allergen> findAll() {
        return allergensManager.findAll();
    }
}
