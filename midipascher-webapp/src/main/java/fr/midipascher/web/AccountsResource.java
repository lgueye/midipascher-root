/**
 * 
 */
package fr.midipascher.web;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	public Response createAccount(final Account account) throws Throwable {

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

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(value = "/account/{accountId}/restaurant/{restaurantId}")
	public Response updateRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId, final Restaurant restaurant) throws Throwable {

		this.facade.updateRestaurant(accountId, restaurantId, restaurant);

		return Response.ok().build();

	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(value = "/account/{accountId}/restaurant/{restaurantId}")
	public Response readRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId) throws Throwable {

		final Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

		return Response.ok(restaurant).build();

	}

	@DELETE
	@Path("/account/{id}")
	public Response deleteAccount(@PathParam(value = "id") final Long id) throws Throwable {

		this.facade.deleteAccount(id);

		return Response.ok().build();

	}

	@PUT
	@Path("/account/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAccount(@PathParam(value = "id") final Long id, final Account account) throws Throwable {

		this.facade.updateAccount(id, account);

		return Response.ok().build();

	}

	@GET
	@Path("/account/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response readAccount(@PathParam(value = "id") final Long id) throws Throwable {

		final Account account = this.facade.readAccount(id, true);

		return Response.ok(account).build();

	}

	@DELETE
	@Path(value = "/account/{accountId}/restaurant/{restaurantId}")
	public Response deleteRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId) throws Throwable {

		this.facade.deleteRestaurant(accountId, restaurantId);

		return Response.ok().build();

	}
}
