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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(value = WebConstants.BACKEND_PATH + AccountsResource.ACCOUNT_COLLECTION_RESOURCE_PATH)
public class AccountsResource {

	@Autowired
	private Facade				facade;

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(AccountsResource.class);

	public static final String	ACCOUNT_COLLECTION_RESOURCE_PATH	= "/accounts";
	public static final String	ACCOUNT_SINGLE_RESOURCE_PATH		= "/{accountId}";
	public static final String	RESTAURANT_COLLECTION_RESOURCE_PATH	= ACCOUNT_SINGLE_RESOURCE_PATH + "/restaurants";
	public static final String	RESTAURANT_SINGLE_RESOURCE_PATH		= RESTAURANT_COLLECTION_RESOURCE_PATH
																			+ "/{restaurantId}";

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createAccount(final Account account) throws Throwable {

		final Long id = this.facade.createAccount(account);

		final URI uri = UriBuilder.fromPath(ACCOUNT_SINGLE_RESOURCE_PATH).build(String.valueOf(id));

		return Response.created(uri).build();

	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(value = RESTAURANT_COLLECTION_RESOURCE_PATH)
	public Response createRestaurant(@PathParam(value = "accountId") final Long accountId, final Restaurant restaurant)
			throws Throwable {

		final Long id = this.facade.createRestaurant(accountId, restaurant);

		final URI uri = UriBuilder.fromPath(RESTAURANT_SINGLE_RESOURCE_PATH).build(String.valueOf(accountId),
				String.valueOf(id));

		return Response.created(uri).build();

	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(RESTAURANT_SINGLE_RESOURCE_PATH)
	public Response updateRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId, final Restaurant restaurant) throws Throwable {

		this.facade.updateRestaurant(accountId, restaurantId, restaurant);

		return Response.ok().build();

	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(RESTAURANT_SINGLE_RESOURCE_PATH)
	public Response readRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId) throws Throwable {

		final Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

		return Response.ok(restaurant).build();

	}

	@DELETE
	@Path(ACCOUNT_SINGLE_RESOURCE_PATH)
	public Response deleteAccount(@PathParam(value = "accountId") final Long id) throws Throwable {

		this.facade.deleteAccount(id);

		return Response.ok().build();

	}

	@PUT
	@Path(ACCOUNT_SINGLE_RESOURCE_PATH)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAccount(@PathParam(value = "accountId") final Long id, final Account account)
			throws Throwable {

		this.facade.updateAccount(id, account);

		return Response.ok().build();

	}

	@GET
	@Path(ACCOUNT_SINGLE_RESOURCE_PATH)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response readAccount(@PathParam(value = "accountId") final Long id) throws Throwable {

		final Account account = this.facade.readAccount(id, true);

		return Response.ok(account).build();

	}

	@DELETE
	@Path(RESTAURANT_SINGLE_RESOURCE_PATH)
	public Response deleteRestaurant(@PathParam(value = "accountId") final Long accountId,
			@PathParam(value = "restaurantId") final Long restaurantId) throws Throwable {

		this.facade.deleteRestaurant(accountId, restaurantId);

		return Response.ok().build();

	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path(ACCOUNT_SINGLE_RESOURCE_PATH + "/lock")
	public Response lockAccount(@PathParam(value = "accountId") final Long id) throws Throwable {

		this.facade.lockAccount(id);

		return Response.ok().build();

	}

}
