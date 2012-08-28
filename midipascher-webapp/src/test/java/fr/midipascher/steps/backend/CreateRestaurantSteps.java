/**
 *
 */
package fr.midipascher.steps.backend;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AccountsResource;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateRestaurantSteps extends BackendBaseSteps {

  private static final String CREATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
      .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("3")
      .path("restaurants").build().toString();

  private static final String INVALID_ACCOUNT_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
      .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("-1")
      .path("restaurants").build().toString();

  /**
   * @param exchange
   */
  public CreateRestaurantSteps(Exchange exchange) {
    super(exchange);
  }

  @Then("the message should be \"<message>\"")
  public void expectedMessage(@Named("message") final String message) {
    this.exchange.assertExpectedMessage(ResponseError.class, message);
  }

  @Then("I should be able to read the new resource")
  public void readTheNewResource() {
    this.exchange.assertNewResource(Restaurant.class);
  }

  @When("I send a \"create restaurant\" request with wrong account")
  public void sendAcreateRestaurantRequestWithWrongAccount() {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setUri(INVALID_ACCOUNT_URI);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong city \"<wrong_city>\"")
  public void sendAcreateRestaurantRequestWithWrongCity(
      @Named("wrong_city") final String wrongCity) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.getAddress().setCity(wrongCity);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong country code \"<wrong_country_code>\"")
  public void sendAcreateRestaurantRequestWithWrongCountryCode(
      @Named("wrong_country_code") final String wrongCountryCode) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.getAddress().setCountryCode(wrongCountryCode);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong description \"<wrong_description>\"")
  public void sendAcreateRestaurantRequestWithWrongDescription(
      @Named("wrong_description") final String wrongDescription) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.setDescription(wrongDescription);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong main offer \"<wrong_main_offer>\"")
  public void sendAcreateRestaurantRequestWithWrongMainOffer(
      @Named("wrong_main_offer") final String wrongMainOffer) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.setMainOffer(wrongMainOffer);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong name \"<wrong_name>\"")
  public void sendAcreateRestaurantRequestWithWrongName(
      @Named("wrong_name") final String wrongName) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.setName(wrongName);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong phone number \"<wrong_phone_number>\"")
  public void sendAcreateRestaurantRequestWithWrongPhoneNumber(
      @Named("wrong_phone_number") final String wrongPhoneNumber) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.setPhoneNumber(wrongPhoneNumber);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a \"create restaurant\" request with wrong postal code \"<wrong_postal_code>\"")
  public void sendAcreateRestaurantRequestWithWrongPostalCode(
      @Named("wrong_postal_code") final String wrongPostalCode) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.getAddress().setPostalCode(wrongPostalCode);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.createEntity();
  }

  @When(
      "I send a \"create restaurant\" request with wrong street address \"<wrong_street_address>\"")
  public void sendAcreateRestaurantRequestWithWrongStreetAddress(
      @Named("wrong_street_address") final String wrongStreetAddress) {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.getAddress().setStreetAddress(wrongStreetAddress);
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

  @When("I send a valid \"create restaurant\" request")
  public void sendAValidcreateRestaurantRequest() {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.clearSpecialties();
    final FoodSpecialty foodSpecialty = new FoodSpecialty();
    foodSpecialty.setId(1L);
    restaurant.addSpecialty(foodSpecialty);
    this.exchange.getRequest().setBody(restaurant);
    this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
    this.exchange.getRequest().setUri(CREATE_URI);
    this.exchange.createEntity();
  }

}
