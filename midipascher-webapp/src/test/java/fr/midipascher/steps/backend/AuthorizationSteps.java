/*
 *
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthorizationSteps extends BackendBaseSteps {

	/**
	 * @param exchange
	 */
	public AuthorizationSteps(Exchange exchange) {
		super(exchange);
	}

	private static final String	URI	= UriBuilder.fromPath(WebConstants.BACKEND_PATH)
											.path(FoodSpecialtiesResource.class).build().toString();

	@Then("the response message should be \"<message>\"")
	public void expectResponseMessage(@Named("message") final String responseMessage) {
		this.exchange.assertExpectedMessage(ResponseError.class, responseMessage);
	}

	@When("I request a protected resource that require ADMIN rights")
	@Alias("I request a protected resource")
	public void requestProtectedResource() {
		final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
		Request request = this.getExchange().getRequest();
		request.setBody(foodSpecialty);
		request.setUri(URI);
		this.exchange.createEntity();
	}

}
