/*
 *
 */
package fr.midipascher.stories.steps;

import java.net.URI;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class Authentication {

	@Autowired
	@Qualifier("baseEndPoint")
	private String			baseEndPoint;
	private URI				uri;
	private FoodSpecialty	body;
	String					requestContentType;
	Client					restClient	= Client.create();
	ClientResponse			response;

	@Given("I authenticate with <uid> uid and $password password")
	public void authenticate(@Named("uid") final String uid, @Named("password") final String password) {
		this.restClient.addFilter(new HTTPBasicAuthFilter(uid, password));
	}

	@Given("I provide a valid create food specialty request body")
	public void provideValidRequestBody() {
		this.body = TestUtils.validFoodSpecialty();
	}

	@Given("I send <request-contenttype>")
	public void provideRequestContentType(@Named("request-contenttype") final String requestContentType) {
		this.requestContentType = requestContentType;
	}

	@When("I send a create food specialty request")
	public void sendRequest() {
		this.uri = URI.create(this.baseEndPoint).resolve("/foodspecialty");
		WebResource webResource = this.restClient.resource(this.uri);
		this.response = webResource.header("Content-Type", this.requestContentType).post(ClientResponse.class,
				this.body);
	}

	@Then("I should get an unsuccessfull response")
	public void responseShouldBeUnsuccessfull() {
		Assert.assertNotNull(this.response);
		Assert.assertNotNull(this.response.getStatus());
		int statusCodeFirstDigit = Integer.valueOf(String.valueOf(this.response.getStatus()).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
	}

	@Then("the response code should be $statusCode")
	public void expectStatusCode(int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@Then("the error message should be <message>")
	public void expectMessage(String message) {
		Assert.assertEquals(message, this.response.getEntity(String.class));
	}
}
