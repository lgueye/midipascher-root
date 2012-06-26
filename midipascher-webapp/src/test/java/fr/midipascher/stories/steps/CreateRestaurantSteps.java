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

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.test.TestUtils;
import fr.midipascher.web.WebConstants;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateRestaurantSteps {

	private ClientResponse		response;
	private String				preferredLanguage;
	private String				preferredFormat;

	private static final String	CREATE_URI			= WebConstants.BACKEND_PATH + "/accounts/3/restaurants";
	private static final String	INVALID_ACCOUNT_URI	= WebConstants.BACKEND_PATH + "/accounts/-1/restaurants";

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		MidipascherClient.setCredentials(uid, password);
	}

	@When("I send a valid \"create restaurant\" request")
	public void sendAValidcreateRestaurantRequest() {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = "application/json";
		String language = "fr";
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@Then("the response code should be \"$status\"")
	public void expectedStatus(@Named("status") int status) {
		Assert.assertEquals(status, this.response.getStatus());
	}

	@Then("I should be able to read the new resource")
	public void readTheNewResource() {
		String responseFormat = "application/json";
		String responseLanguage = "en";
		this.response = MidipascherClient.readLocation(this.response.getLocation(), responseFormat, responseLanguage);
		Assert.assertEquals(200, this.response.getStatus());
		Assert.assertNotNull(this.response.getEntity(Restaurant.class));
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
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, INVALID_ACCOUNT_URI, requestContentType, format,
				language);
	}

	@When("I send a \"create restaurant\" request with wrong name \"<wrong_name>\"")
	public void sendAcreateRestaurantRequestWithWrongName(@Named("wrong_name") String wrongName) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.setName(wrongName);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong description \"<wrong_description>\"")
	public void sendAcreateRestaurantRequestWithWrongDescription(@Named("wrong_description") String wrongDescription) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.setDescription(wrongDescription);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong phone number \"<wrong_phone_number>\"")
	public void sendAcreateRestaurantRequestWithWrongPhoneNumber(@Named("wrong_phone_number") String wrongPhoneNumber) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.setPhoneNumber(wrongPhoneNumber);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong main offer \"<wrong_main_offer>\"")
	public void sendAcreateRestaurantRequestWithWrongMainOffer(@Named("wrong_main_offer") String wrongMainOffer) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.setMainOffer(wrongMainOffer);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong street address \"<wrong_street_address>\"")
	public void sendAcreateRestaurantRequestWithWrongStreetAddress(
			@Named("wrong_street_address") String wrongStreetAddress) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getAddress().setStreetAddress(wrongStreetAddress);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong city \"<wrong_city>\"")
	public void sendAcreateRestaurantRequestWithWrongCity(@Named("wrong_city") String wrongCity) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getAddress().setCity(wrongCity);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong postal code \"<wrong_postal_code>\"")
	public void sendAcreateRestaurantRequestWithWrongPostalCode(@Named("wrong_postal_code") String wrongPostalCode) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getAddress().setPostalCode(wrongPostalCode);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

	@When("I send a \"create restaurant\" request with wrong country code \"<wrong_country_code>\"")
	public void sendAcreateRestaurantRequestWithWrongCountryCode(@Named("wrong_country_code") String wrongCountryCode) {
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getAddress().setCountryCode(wrongCountryCode);
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = this.preferredFormat;
		String language = this.preferredLanguage;
		String requestContentType = "application/xml";
		this.response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
	}

}
