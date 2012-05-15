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
public class DeleteRestaurantSteps {

	private ClientResponse	response;
	private String			preferredLanguage;
	private String			preferredFormat;
	private final String	restaurantURI	= "/account/2/restaurant/2";

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@When("I send a valid \"delete restaurant\" request")
	public void sendAValidcreateRestaurantRequest() {
		String format = "application/json";
		String language = "fr";
		this.response = MidipascherClient.deleteEntity(this.restaurantURI, format, language);
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
	public void sendAnDeleteRestaurantRequestWithWrongAccount() {
		final String relativePath = "/account/-1/restaurant/1";
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		this.response = MidipascherClient.deleteEntity(relativePath, format, language);
	}

	@When("I send a \"delete restaurant\" request with wrong id \"<wrong_id>\"")
	public void sendAnDeleteRestaurantRequestWithWrongId(@Named("wrong_id") final Long id) {
		final String relativePath = "/account/2/restaurant/-1";
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		this.response = MidipascherClient.deleteEntity(relativePath, format, language);
	}

}
