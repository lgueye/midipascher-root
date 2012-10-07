/**
 *
 */
package fr.midipascher.web.resources;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.business.Facade;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(FoodSpecialtiesResource.COLLECTION_RESOURCE_PATH)
@Scope("request")
public class FoodSpecialtiesResource {

    @Autowired
    private Facade facade;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodSpecialtiesResource.class);

    public static final String COLLECTION_RESOURCE_PATH = "/foodspecialties";

    public static final String SINGLE_RESOURCE_PATH = "/{id}";

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response create(final FoodSpecialty foodSpecialty) throws Throwable {

        final Long id = facade.createFoodSpecialty(foodSpecialty);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

        return Response.created(uri).build();

    }

    @DELETE
    @Path(SINGLE_RESOURCE_PATH)
    public Response delete(@PathParam(value = "id") final Long id) throws Throwable {

        facade.deleteFoodSpecialty(id);

        return Response.noContent().build();

    }

    @POST
    @Path("/search")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response find(final @FormParam("code") String code, final @FormParam("label") String label,
                         final @FormParam("active") boolean active) throws Throwable {

        final List<FoodSpecialty> results = facade.listFoodSpecialties();

        final GenericEntity<List<FoodSpecialty>> entity = new GenericEntity<List<FoodSpecialty>>(results) {
        };

        if (CollectionUtils.isEmpty(results)) {
            FoodSpecialtiesResource.LOGGER.info("No results found");
        }

        return Response.ok(entity).build();

    }

    @GET
    @Path(SINGLE_RESOURCE_PATH)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response get(@PathParam(value = "id") final Long id) throws Throwable {

        final FoodSpecialty foodSpecialty = facade.readFoodSpecialty(id);

        return Response.ok(foodSpecialty).build();

    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response list() throws Throwable {
        final List<FoodSpecialty> results = facade.listFoodSpecialties();
        final GenericEntity<List<FoodSpecialty>> entity = new GenericEntity<List<FoodSpecialty>>(results) {};
        return Response.ok(entity).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path(SINGLE_RESOURCE_PATH + "/inactivate")
    public Response inactivate(@PathParam(value = "id") final Long id) throws Throwable {

        facade.inactivateFoodSpecialty(id);

        return Response.ok().build();

    }

    @PUT
    @Path(SINGLE_RESOURCE_PATH)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(@PathParam(value = "id") final Long id, final FoodSpecialty foodSpecialty) throws Throwable {

        facade.updateFoodSpecialty(id, foodSpecialty);

        return Response.ok().build();

    }

}
