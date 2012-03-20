/*
 *
 */
package fr.midipascher.stories.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class ContentNegotiationSteps {

	private String			responseContentType;
	private String			requestContentType;
	private ClientResponse	response;

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@Then("I should get my newly created resource")
	public void getResourceAtLocation() {
		String responseFormat = this.responseContentType;
		String responseLanguage = "en";
		this.response = MidipascherClient.readLocation(this.response.getLocation(), responseFormat, responseLanguage);
		Assert.assertNotNull(this.response.getEntity(FoodSpecialty.class));
	}

	@When("I send a create request")
	public void sendCreateRequest() {
		String requestFormat = this.requestContentType;
		String responseFormat = "application/json";
		String responseLanguage = "en";
		MidipascherClient.setCredentials("admin", "secret");
		this.response = MidipascherClient.createEntity(TestUtils.validFoodSpecialty(), "/foodspecialty", requestFormat,
				responseFormat, responseLanguage);
	}

	@When("I send a search request")
	public void sendSearchRequest() {
		final String path = "/foodspecialty/find";
		final String requestContentType = "application/x-www-form-urlencoded";
		String queryString = "";
		this.response = MidipascherClient.findEntityByCriteria(path, queryString, requestContentType,
				this.responseContentType, "en");
	}

	@Given("I send \"<requestContentType>\" data")
	public void setRequestContentType(@Named("requestContentType") final String requestContentType) {
		this.requestContentType = requestContentType;
	}

	@Given("I receive \"<responseContentType>\" data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

}
