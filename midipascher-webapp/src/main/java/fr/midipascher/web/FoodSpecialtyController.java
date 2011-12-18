/**
 * 
 */
package fr.midipascher.web;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(value = "/foodspecialty")
@Scope("request")
public class FoodSpecialtyController {

	@Autowired
	private Facade				facade;

	@Context
	private UriInfo				uriInfo;

	private static final Logger	LOGGER	= LoggerFactory.getLogger(FoodSpecialtyController.class);

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response create(final FoodSpecialty foodSpecialty) throws Throwable {

		final Long id = this.facade.createFoodSpecialty(foodSpecialty);

		final URI uri = this.uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam(value = "id") final Long id) throws Throwable {

		this.facade.deleteFoodSpecialty(id);

		return Response.noContent().build();

	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@PathParam(value = "id") final Long id) throws Throwable {

		final FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(id);

		if (foodSpecialty == null) return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(foodSpecialty).build();

	}

	@POST
	@Path(value = "find")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response find(final @FormParam("code") String code, final @FormParam("label") String label,
			final @FormParam("active") boolean active) throws Throwable {

		final List<FoodSpecialty> results = this.facade.listFoodSpecialties();

		final GenericEntity<List<FoodSpecialty>> entity = new GenericEntity<List<FoodSpecialty>>(results) {
		};

		if (CollectionUtils.isEmpty(results)) FoodSpecialtyController.LOGGER.info("No results found");

		return Response.ok(entity).build();

	}

}
