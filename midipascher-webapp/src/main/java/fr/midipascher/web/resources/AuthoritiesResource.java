/**
 * 
 */
package fr.midipascher.web.resources;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.Authority;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(AuthoritiesResource.COLLECTION_RESOURCE_PATH)
@Scope("request")
public class AuthoritiesResource {

    @Autowired
    private Facade facade;

    @Context
    private UriInfo uriInfo;

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(AuthoritiesResource.class);

    public static final String COLLECTION_RESOURCE_PATH = "/authorities";

    public static final String SINGLE_RESOURCE_PATH = "/{id}";

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response create(final Authority authority) throws Throwable {

        final Long id = facade.createAuthority(authority);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

        return Response.created(uri).build();

    }

    @GET
    @Path(AuthoritiesResource.SINGLE_RESOURCE_PATH)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response get(@PathParam(value = "id") final Long id) throws Throwable {

        final Authority authority = facade.readAuthority(id);

        if (authority == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(authority).build();

    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path(AuthoritiesResource.SINGLE_RESOURCE_PATH + "/inactivate")
    public Response inactivate(@PathParam(value = "id") final Long id) throws Throwable {

        facade.inactivateAuthority(id);

        return Response.ok().build();

    }

}
