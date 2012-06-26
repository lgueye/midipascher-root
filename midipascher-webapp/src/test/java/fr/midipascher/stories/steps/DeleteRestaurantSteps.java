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
import fr.midipascher.web.WebConstants;

/**
 * @author louis.gueye@gmail.com
 */
public class DeleteRestaurantSteps {

	private ClientResponse		response;
	private String				preferredLanguage;
	private String				preferredFormat;
	private static final String	DELETE_URI						= WebConstants.BACKEND_PATH
																		+ "/accounts/3/restaurants/2";
	private static final String	INVALID_ACCOUNT_DELETE_URI		= WebConstants.BACKEND_PATH
																		+ "/accounts/-1/restaurants/2";
	private static final String	INVALID_RESTAURANT_DELETE_URI	= WebConstants.BACKEND_PATH
																		+ "/accounts/3/restaurants/-1";

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@When("I send a valid \"delete restaurant\" request")
	public void sendAValidcreateRestaurantRequest() {
		String format = "application/json";
		String language = "fr";
		this.response = MidipascherClient.deleteEntity(DELETE_URI, format, language);
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

	@When("I send a \"delete restaurant\" request with wrong account")
	public void sendADeleteRestaurantRequestWithWrongAccount() {
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		this.response = MidipascherClient.deleteEntity(INVALID_ACCOUNT_DELETE_URI, format, language);
	}

	@When("I send a \"delete restaurant\" request with wrong id \"<wrong_id>\"")
	public void sendADeleteRestaurantRequestWithWrongId(@Named("wrong_id") final Long id) {
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		this.response = MidipascherClient.deleteEntity(INVALID_RESTAURANT_DELETE_URI, format, language);
	}

}
