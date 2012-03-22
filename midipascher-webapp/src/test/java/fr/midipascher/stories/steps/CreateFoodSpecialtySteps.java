/**
 * 
 */
package fr.midipascher.stories.steps;

import org.jbehave.core.annotations.AfterScenario;
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
public class CreateFoodSpecialtySteps {

	private ClientResponse	response;
	private String			responseLanguage;
	private String			responseContentType;

	@AfterScenario
	public void afterScenario() {
		this.responseLanguage = null;
		this.responseContentType = null;
	}

	@Given("I authenticate with \"$uid\" uid and \"$password\" password")
	public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@Then("the message should be \"<message>\"")
	public void expectedMessage(@Named("message") final String message) {
		Assert.assertEquals(message, this.response.getEntity(ResponseError.class).getMessage());
	}

	@Then("I should be able to read the new resource")
	public void expectedNewResource() {
		String responseFormat = this.responseContentType;
		String responseLanguage = "en";
		this.response = MidipascherClient.readLocation(this.response.getLocation(), responseFormat, responseLanguage);
		Assert.assertEquals(200, this.response.getStatus());
		Assert.assertNotNull(this.response.getEntity(FoodSpecialty.class));
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@When("I send a \"create food specialty\" request with wrong code \"<wrong_code>\"")
	public void sendCreateFoodSpecialtyRequestWithWrongCode(@Named("wrong_code") final String code) {
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		foodSpecialty.setCode(code);
		final String requestContentType = "application/json";
		String relativePath = "/foodspecialty";
		this.response = MidipascherClient.createEntity(foodSpecialty, relativePath, requestContentType,
				this.responseContentType, this.responseLanguage);
	}

	@When("I send a \"create food specialty\" request with wrong label \"<wrong_label>\"")
	public void sendCreateFoodSpecialtyRequestWithWrongLabel(@Named("wrong_label") final String label) {
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		foodSpecialty.setLabel(label);
		final String requestContentType = "application/json";
		String relativePath = "/foodspecialty";
		this.response = MidipascherClient.createEntity(foodSpecialty, relativePath, requestContentType,
				this.responseContentType, this.responseLanguage);
	}

	@When("I send a valid \"create food specialty\" request")
	public void sendValidCreateFoodSpecialtyRequest() {
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		final String requestContentType = "application/json";
		String relativePath = "/foodspecialty";
		this.response = MidipascherClient.createEntity(foodSpecialty, relativePath, requestContentType,
				this.responseContentType, this.responseLanguage);
	}

	@Given("I accept \"$responseContentType\" format")
	public void setFormat(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

	@Given("I accept \"$responseLanguage\" language")
	public void setLanguage(@Named("responseLanguage") final String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}
}
