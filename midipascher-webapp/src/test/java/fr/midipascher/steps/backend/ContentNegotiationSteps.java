/*
 *
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class ContentNegotiationSteps extends BackendBaseSteps {

	private static final String	CREATE_URI	= UriBuilder.fromPath(WebConstants.BACKEND_PATH)
													.path(FoodSpecialtiesResource.class).build().toString();

	private static final String	SEARCH_URI	= UriBuilder.fromPath(WebConstants.BACKEND_PATH)
													.path(FoodSpecialtiesResource.class).path("search").build()
													.toString();

	public ContentNegotiationSteps(Exchange exchange) {
		super(exchange);
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		this.exchange.assertExpectedStatus(statusCode);
	}

	@Then("I should get my newly created resource")
	public void getResourceAtLocation() {
		this.exchange.assertNewResource(FoodSpecialty.class);
	}

	@SuppressWarnings("unchecked")
	@When("I send a create request")
	public void sendCreateRequest() {
		this.exchange.setCredentials("admin@admin.com", "secret");
		this.exchange.getRequest().setBody(TestFixtures.validFoodSpecialty());
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@SuppressWarnings("unchecked")
	@When("I send a search request")
	public void sendSearchRequest() {
		this.exchange.getRequest().setUri(SEARCH_URI);
		this.exchange.getRequest().setBody("");
		setRequestContentType("application/x-www-form-urlencoded");
		this.exchange.findEntityByCriteria();
	}

	@Given("I send \"<requestContentType>\" data")
	public void setRequestContentType(@Named("requestContentType") final String requestContentType) {
		this.exchange.getRequest().setType(requestContentType);
	}

	@Given("I receive \"<responseContentType>\" data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.exchange.getRequest().setRequestedType(responseContentType);
	}

}
