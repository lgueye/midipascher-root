/*
 *
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.When;

import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AuthoritiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthenticationSteps extends BackendBaseSteps {

	/**
	 * @param exchange
	 */
	public AuthenticationSteps(Exchange exchange) {
		super(exchange);
	}

	private static final String	URI	= UriBuilder.fromPath(WebConstants.BACKEND_PATH).path(AuthoritiesResource.class)
											.build().toString();

	@When("I request a protected resource")
	public void requestProtectedResource() {
		this.getExchange().getRequest().setUri(URI);
		this.getExchange().getRequest().setRequestedType("application/json");
		this.getExchange().readURI();
	}

}
