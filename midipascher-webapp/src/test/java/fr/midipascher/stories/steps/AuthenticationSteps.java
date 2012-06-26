/*
 *
 */
package fr.midipascher.stories.steps;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.web.AuthoritiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthenticationSteps {

	private String				responseLanguage;
	private ClientResponse		response;

	private static final String	URI	= UriBuilder.fromResource(AuthoritiesResource.class).build().toString();

	@Given("I authenticate with \"<uid>\" uid and \"<password>\" password")
	public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@Given("I accept \"<responseLanguage>\" language")
	public void setAcceptLanguage(@Named("responseLanguage") final String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}

	@When("I request a protected resource")
	public void requestProtectedResource() {
		this.response = MidipascherClient.readURI(URI, "application/json", this.responseLanguage);
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		MidipascherClient.expectedCode(statusCode, this.response.getStatus());
	}

	@Then("the response message should be \"<message>\"")
	public void expectResponseMessage(@Named("message") final String responseMessage) {
		String error = this.response.getEntity(String.class);
		MidipascherClient.expectedMessage(responseMessage, error);
	}

}
