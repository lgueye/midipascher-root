/**
 * 
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

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
import fr.midipascher.web.resources.AccountsResource;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateRestaurantSteps {

    private ClientResponse response;
    private String preferredLanguage;
    private String preferredFormat;

    private static final String CREATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("3").path("restaurants").build().toString();

    private static final String INVALID_ACCOUNT_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("-1").path("restaurants").build().toString();

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        Assert.assertEquals(message, response.getEntity(ResponseError.class).getMessage());
    }

    @Then("the response code should be \"$status\"")
    public void expectedStatus(@Named("status") final int status) {
        Assert.assertEquals(status, response.getStatus());
    }

    @Given("I accept \"$preferredFormat\" format")
    public void preferredFormat(@Named("preferredFormat") final String preferredFormat) {
        this.preferredFormat = preferredFormat;
    }

    @Given("I accept \"$preferredLanguage\" language")
    public void preferredLanguage(@Named("language") final String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @Given("I provide \"$uid\" uid and \"$password\" password")
    public void provideAuthInformations(@Named("uid") final String uid, @Named("password") final String password) {
        MidipascherClient.setCredentials(uid, password);
    }

    @Then("I should be able to read the new resource")
    public void readTheNewResource() {
        final String responseFormat = "application/json";
        final String responseLanguage = "en";
        response = MidipascherClient.readLocation(response.getLocation(), responseFormat, responseLanguage);
        Assert.assertEquals(200, response.getStatus());
        Assert.assertNotNull(response.getEntity(Restaurant.class));
    }

    @When("I send a \"create restaurant\" request with wrong account")
    public void sendAcreateRestaurantRequestWithWrongAccount() {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient
                .createEntity(restaurant, INVALID_ACCOUNT_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong city \"<wrong_city>\"")
    public void sendAcreateRestaurantRequestWithWrongCity(@Named("wrong_city") final String wrongCity) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.getAddress().setCity(wrongCity);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong country code \"<wrong_country_code>\"")
    public void sendAcreateRestaurantRequestWithWrongCountryCode(
            @Named("wrong_country_code") final String wrongCountryCode) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.getAddress().setCountryCode(wrongCountryCode);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong description \"<wrong_description>\"")
    public void sendAcreateRestaurantRequestWithWrongDescription(
            @Named("wrong_description") final String wrongDescription) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.setDescription(wrongDescription);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong main offer \"<wrong_main_offer>\"")
    public void sendAcreateRestaurantRequestWithWrongMainOffer(@Named("wrong_main_offer") final String wrongMainOffer) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.setMainOffer(wrongMainOffer);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong name \"<wrong_name>\"")
    public void sendAcreateRestaurantRequestWithWrongName(@Named("wrong_name") final String wrongName) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.setName(wrongName);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong phone number \"<wrong_phone_number>\"")
    public void sendAcreateRestaurantRequestWithWrongPhoneNumber(
            @Named("wrong_phone_number") final String wrongPhoneNumber) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.setPhoneNumber(wrongPhoneNumber);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong postal code \"<wrong_postal_code>\"")
    public void sendAcreateRestaurantRequestWithWrongPostalCode(@Named("wrong_postal_code") final String wrongPostalCode) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.getAddress().setPostalCode(wrongPostalCode);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a \"create restaurant\" request with wrong street address \"<wrong_street_address>\"")
    public void sendAcreateRestaurantRequestWithWrongStreetAddress(
            @Named("wrong_street_address") final String wrongStreetAddress) {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.getAddress().setStreetAddress(wrongStreetAddress);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = preferredFormat;
        final String language = preferredLanguage;
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

    @When("I send a valid \"create restaurant\" request")
    public void sendAValidcreateRestaurantRequest() {
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        final String format = "application/json";
        final String language = "fr";
        final String requestContentType = "application/xml";
        response = MidipascherClient.createEntity(restaurant, CREATE_URI, requestContentType, format, language);
    }

}
