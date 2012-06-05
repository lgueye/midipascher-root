/**
 * 
 */
package fr.midipascher.stories.steps;

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
public class DeleteAccountSteps {

	private ClientResponse	response;
	private String			preferredLanguage;
	private String			preferredFormat;
	private final String	accountURI	= "/account/6";

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@When("I send a valid \"delete account\" request")
	public void sendAValidcreateAccountRequest() {
		String format = "application/json";
		String language = "fr";
		this.response = MidipascherClient.deleteEntity(this.accountURI, format, language);
	}

	@Then("the response code should be \"$status\"")
	public void expectedStatus(@Named("status") int status) {
		Assert.assertEquals(status, this.response.getStatus());
	}

	@Given("I accept \"$preferredLanguage\" language")
	public void preferredLanguage(@Named("language") String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	@Given("I accept \"$preferredFormat\" format")
	public void preferredFormat(@Named("preferredFormat") String preferredFormat) {
		this.preferredFormat = preferredFormat;
	}

	@Then("the message should be \"<message>\"")
	public void expectedMessage(@Named("message") String message) {
		Assert.assertEquals(message, this.response.getEntity(ResponseError.class).getMessage());
	}

	@When("I send a \"delete account\" request with wrong account")
	public void sendAnDeleteAccountRequestWithWrongAccount() {
		final String relativePath = "/account/-1/account/1";
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		this.response = MidipascherClient.deleteEntity(relativePath, format, language);
	}

	@When("I send a \"delete account\" request with wrong id \"<wrong_id>\"")
	public void sendAnDeleteAccountRequestWithWrongId(@Named("wrong_id") final Long id) {
		final String relativePath = "/account/-1";
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		this.response = MidipascherClient.deleteEntity(relativePath, format, language);
	}

}
