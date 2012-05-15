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

import fr.midipascher.domain.ResponseError;

/**
 * @author louis.gueye@gmail.com
 */
public class LockAccountSteps {

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

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@When("I send a valid \"lock account\" request")
	public void sendValidLockAccountRequest() {
		final String requestContentType = "application/json";
		String relativePath = "/accounts/4/lock";
		this.response = MidipascherClient.lockEntity(relativePath, requestContentType, this.responseContentType,
				this.responseLanguage);
	}

	@When("I send a \"lock account\" request with wrong id \"<wrong_id>\"")
	public void sendLockAccountRequestWithWrongId(@Named("wrong_id") final Long id) {
		final String requestContentType = "application/json";
		String relativePath = "/accounts/" + id + "/lock";
		this.response = MidipascherClient.lockEntity(relativePath, requestContentType, this.responseContentType,
				this.responseLanguage);
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
