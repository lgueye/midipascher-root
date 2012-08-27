/*
 *
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

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

	@Then("I should get my newly created resource")
	public void getResourceAtLocation() {
		this.exchange.assertNewResource(FoodSpecialty.class);
	}

	@When("I send a create request")
	public void sendCreateRequest() {
		this.exchange.setCredentials("admin@admin.com", "secret");
		this.exchange.getRequest().setBody(TestFixtures.validFoodSpecialty());
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a search request")
	public void sendSearchRequest() {
		this.exchange.getRequest().setUri(SEARCH_URI);
		this.exchange.getRequest().setBody("");
		this.exchange.getRequest().setType("application/x-www-form-urlencoded");
		this.exchange.findEntityByCriteria();
	}

}
