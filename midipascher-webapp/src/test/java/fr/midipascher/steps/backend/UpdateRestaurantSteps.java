/**
 *
 */
package fr.midipascher.steps.backend;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AccountsResource;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public class UpdateRestaurantSteps extends BackendBaseSteps {

    private static final String UPDATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("3").path("restaurants").path("1")
            .build().toString();

    private static final
    String
            INVALID_ACCOUNT_UPDATE_URI =
            UriBuilder.fromPath(WebConstants.BACKEND_PATH)
                    .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("-1").path("restaurants").path("1")
                    .build()
                    .toString();

    private static final
    String
            INVALID_RESTAURANT_UPDATE_URI =
            UriBuilder.fromPath(WebConstants.BACKEND_PATH)
                    .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("3").path("restaurants").path("-1")
                    .build()
                    .toString();

    public UpdateRestaurantSteps(Exchange exchange) {
        super(exchange);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        this.exchange.assertExpectedMessage(ResponseError.class, message);
    }

    @When("I send a \"update restaurant\" request with wrong account")
    public void sendAnUpdateRestaurantRequestWithWrongAccount() {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(INVALID_ACCOUNT_UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong city \"<wrong_city>\"")
    public void sendAnUpdateRestaurantRequestWithWrongCity(
            @Named("wrong_city") final String wrongCity) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setCity(wrongCity);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong country code \"<wrong_country_code>\"")
    public void sendAnUpdateRestaurantRequestWithWrongCountryCode(
            @Named("wrong_country_code") final String wrongCountryCode) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setCountryCode(wrongCountryCode);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong description \"<wrong_description>\"")
    public void sendAnUpdateRestaurantRequestWithWrongDescription(
            @Named("wrong_description") final String wrongDescription) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setDescription(wrongDescription);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong id \"<wrong_id>\"")
    public void sendAnUpdateRestaurantRequestWithWrongId(@Named("wrong_id") final Long id) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(INVALID_RESTAURANT_UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong main offer \"<wrong_main_offer>\"")
    public void sendAnUpdateRestaurantRequestWithWrongMainOffer(
            @Named("wrong_main_offer") final String wrongMainOffer) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setMainOffer(wrongMainOffer);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong name \"<wrong_name>\"")
    public void sendAnUpdateRestaurantRequestWithWrongName(
            @Named("wrong_name") final String wrongName) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setName(wrongName);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong phone number \"<wrong_phone_number>\"")
    public void sendAnUpdateRestaurantRequestWithWrongPhoneNumber(
            @Named("wrong_phone_number") final String wrongPhoneNumber) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setPhoneNumber(wrongPhoneNumber);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a \"update restaurant\" request with wrong postal code \"<wrong_postal_code>\"")
    public void sendAnUpdateRestaurantRequestWithWrongPostalCode(
            @Named("wrong_postal_code") final String wrongPostalCode) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setPostalCode(wrongPostalCode);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When(
            "I send a \"update restaurant\" request with wrong street address \"<wrong_street_address>\"")
    public void sendAnUpdateRestaurantRequestWithWrongStreetAddress(
            @Named("wrong_street_address") final String wrongStreetAddress) {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setStreetAddress(wrongStreetAddress);
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

    @When("I send a valid \"update restaurant\" request")
    public void sendAValidcreateRestaurantRequest() {
        final Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.clearSpecialties();
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setId(1L);
        restaurant.addSpecialty(foodSpecialty);
        exchange.getRequest().setBody(restaurant);
        exchange.getRequest().setUri(UPDATE_URI);
        exchange.updateEntity();
    }

}
