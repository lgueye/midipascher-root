package fr.midipascher.web.resources;

import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(SearchRestaurantsResource.RESOURCE_PATH)
@Scope("request")
public class SearchRestaurantsResource {

    public static final String RESOURCE_PATH = "/restaurants/search";

    @Autowired
    private Facade facade;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchRestaurantsResource.class);

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response find(final Restaurant criteria) throws Throwable {

        final List<Restaurant> results = facade.findRestaurantsByCriteria(criteria);

        final GenericEntity<List<Restaurant>> entity = new GenericEntity<List<Restaurant>>(results) {
        };

        if (CollectionUtils.isEmpty(results)) {
            SearchRestaurantsResource.LOGGER.info("No results found");
        }

        return Response.ok(entity).build();

    }
}
