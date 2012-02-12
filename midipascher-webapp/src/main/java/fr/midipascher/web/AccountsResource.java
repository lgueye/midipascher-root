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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.User;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(value = "/accounts")
@Scope("request")
public class AccountsResource {

	@Autowired
	private Facade				facade;

	@Context
	private UriInfo				uriInfo;

	private static final Logger	LOGGER	= LoggerFactory.getLogger(AccountsResource.class);

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response create(final User user) throws Throwable {

		final Long id = this.facade.createAccount(user);

		final URI uri = this.uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam(value = "id") final Long id) throws Throwable {

		this.facade.deleteAccount(id);

		return Response.noContent().build();

	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@PathParam(value = "id") final Long id) throws Throwable {

		final User account = this.facade.readAccount(id);

		if (account == null) return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(account).build();

	}

}
