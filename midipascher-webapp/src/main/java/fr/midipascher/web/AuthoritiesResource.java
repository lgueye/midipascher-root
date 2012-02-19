/**
 * 
 */
package fr.midipascher.web;

import java.net.URI;

import javax.ws.rs.Consumes;
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

import fr.midipascher.domain.Authority;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(value = "/admin/authorities")
@Scope("request")
public class AuthoritiesResource {

	@Autowired
	private Facade				facade;

	@Context
	private UriInfo				uriInfo;

	private static final Logger	LOGGER	= LoggerFactory.getLogger(AuthoritiesResource.class);

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response create(final Authority authority) throws Throwable {

		final Long id = this.facade.createAuthority(authority);

		final URI uri = this.uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@PathParam(value = "id") final Long id) throws Throwable {

		final Authority authority = this.facade.readAuthority(id);

		if (authority == null) return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(authority).build();

	}

}