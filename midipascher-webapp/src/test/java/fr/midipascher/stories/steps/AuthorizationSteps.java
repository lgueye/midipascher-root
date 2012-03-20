/*
 *
 */
package fr.midipascher.stories.steps;

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthorizationSteps {

	private String			responseLanguage;
	private ClientResponse	response;
	private String			responseContentType;

	@Given("I authenticate with \"$uid\" uid and \"$password\" password")
	public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@Given("I accept \"<responseLanguage>\" language")
	public void setAcceptLanguage(@Named("responseLanguage") final String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}

	@When("I request a protected resource that require ADMIN rights")
	@Alias("I request a protected resource")
	public void requestProtectedResource() {
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		final String path = "/foodspecialty";
		final String requestContentType = "application/json";
		this.response = MidipascherClient.createEntity(foodSpecialty, path, requestContentType,
				this.responseContentType, this.responseLanguage);
	}

	@Given("I receive \"<responseContentType>\" data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@Then("the response message should be \"<message>\"")
	public void expectResponseMessage(@Named("message") final String responseMessage) {
		ResponseError error = this.response.getEntity(ResponseError.class);
		Assert.assertNotNull(error);
		Assert.assertNotNull(error.getMessage());
		Assert.assertEquals(responseMessage, error.getMessage().trim());
	}

}
