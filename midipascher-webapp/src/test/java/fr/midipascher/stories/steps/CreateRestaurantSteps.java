/**
 * 
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateRestaurantSteps {

	private ClientResponse	response;
	private String			uid;
	private String			password;
	private String			preferredLanguage;
	private String			preferredFormat;
	private final String	baseEndPoint	= ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
	private String			accountURI;

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		this.uid = uid;
		this.password = password;
	}

	@When("I send a valid \"create restaurant\" request")
	public void sendAValidcreateRestaurantRequest() {
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter(this.uid, this.password));
		final String path = this.accountURI + "/restaurants";
		final URI uri = URI.create(path);
		final WebResource webResource = jerseyClient.resource(uri);
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.clearSpecialties();
		FoodSpecialty foodSpecialty = new FoodSpecialty();
		foodSpecialty.setId(1L);
		restaurant.addSpecialty(foodSpecialty);
		String format = "application/json";
		String language = "fr";
		String requestContentType = "application/xml";
		ClientResponse myResponse = jerseyClient.resource(this.baseEndPoint + "/foodspecialty/1")
				.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).get(ClientResponse.class);
		Assert.assertNotNull(myResponse);
		FoodSpecialty entity = myResponse.getEntity(FoodSpecialty.class);
		Assert.assertNotNull(entity);

		webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, restaurant);
	}

	@BeforeStory
	public void setup() {
		this.accountURI = TestUtils.createAccount();
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
