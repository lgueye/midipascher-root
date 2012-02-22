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
public class CreateRestaurantSteps {

	private ClientResponse	response;
	private String			uid;
	private String			password;
	private String			preferredLanguage;
	private Object			preferredFormat;

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		this.uid = uid;
		this.password = password;
	}

	@When("I send a valid \"create restaurant\" request")
	public void sendAValidcreateRestaurantRequest() {
		// PENDING
	}

	@Then("the response code should be \"$status\"")
	public void expectedStatus(@Named("status") int status) {
		Assert.assertEquals(status, this.response.getStatus());
	}

	@Then("I should be able to read the new resource")
	public void readTheNewResource() {
		// PENDING
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

	@When("I send a \"create restaurant\" request with wrong account")
	public void sendAcreateRestaurantRequestWithWrongAccount() {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong name \"<wrong_name>\"")
	public void sendAcreateRestaurantRequestWithWrongName(@Named("wrong_name") String wrongName) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong description \"<wrong_description>\"")
	public void sendAcreateRestaurantRequestWithWrongDescription(@Named("wrong_description") String wrongDescription) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong phone number \"<wrong_phone_number>\"")
	public void sendAcreateRestaurantRequestWithWrongPhoneNumber(@Named("wrong_phone_number") String wrongPhoneNumber) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong main offer \"<wrong_main_offer>\"")
	public void sendAcreateRestaurantRequestWithWrongMainOffer(@Named("wrong_main_offer") String wrongMainOffer) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong street address \"<wrong_street_address>\"")
	public void sendAcreateRestaurantRequestWithWrongStreetAddress(
			@Named("wrong_street_address") String wrongStreetAddress) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong city \"<wrong_city>\"")
	public void sendAcreateRestaurantRequestWithWrongCity(@Named("wrong_city") String wrongCity) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong postal code \"<wrong_postal_code>\"")
	public void sendAcreateRestaurantRequestWithWrongPostalCode(@Named("wrong_postal_code") String wrongPostalCode) {
		// PENDING
	}

	@When("I send a \"create restaurant\" request with wrong country code \"<wrong_country_code>\"")
	public void sendAcreateRestaurantRequestWithWrongCountryCode(@Named("wrong_country_code") String wrongCountryCode) {
		// PENDING
	}

}
