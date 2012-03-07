/**
 * 
 */
package fr.midipascher.web;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(value = "/")
public class AccountsResource {

	@Autowired
	private Facade	facade;

	@Context
	private UriInfo	uriInfo;

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(AccountsResource.class);

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(value = "/accounts")
	public Response create(final Account account) throws Throwable {

		final Long id = this.facade.createAccount(account);

		final URI uri = this.uriInfo.getBaseUriBuilder().path("account").path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(value = "/account/{accountId}/restaurants")
	public Response createRestaurant(@PathParam(value = "accountId") final Long accountId, final Restaurant restaurant)
			throws Throwable {

		final Long id = this.facade.createRestaurant(accountId, restaurant);

		final URI uri = this.uriInfo.getBaseUriBuilder().path("account").path(accountId.toString()).path("restaurant")
				.path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(value = "/account/{accountId}/restaurant/{restaurantId}")
	public Response getRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId) throws Throwable {

		final Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

		if (restaurant == null) return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(restaurant).build();

	}

	@DELETE
	@Path("/account/{id}")
	public Response delete(@PathParam(value = "id") final Long id) throws Throwable {

		this.facade.deleteAccount(id);

		return Response.noContent().build();

	}

	@GET
	@Path("/account/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@PathParam(value = "id") final Long id) throws Throwable {

		final Account account = this.facade.readAccount(id, true);

		if (account == null) return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(account).build();

	}

}
