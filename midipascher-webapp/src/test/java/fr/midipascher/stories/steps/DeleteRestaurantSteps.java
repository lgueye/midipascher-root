/**
 * 
 */
package fr.midipascher.stories.steps;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.domain.ResponseError;
import fr.midipascher.web.AccountsResource;
import fr.midipascher.web.WebConstants;

/**
 * @author louis.gueye@gmail.com
 */
public class DeleteRestaurantSteps {

    private ClientResponse response;
    private String preferredLanguage;
    private String preferredFormat;
    // private static final String DELETE_URI = WebConstants.BACKEND_PATH + "/accounts/3/restaurants/2";
    // private static final String INVALID_ACCOUNT_DELETE_URI = WebConstants.BACKEND_PATH +
    // "/accounts/-1/restaurants/2";
    // private static final String INVALID_RESTAURANT_DELETE_URI = WebConstants.BACKEND_PATH
    // + "/accounts/3/restaurants/-1";

    private static final String DELETE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("3").path("restaurants").path("2").build().toString();

    private static final String INVALID_ACCOUNT_DELETE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("-1").path("restaurants").path("2").build()
            .toString();

    private static final String INVALID_RESTAURANT_DELETE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("3").path("restaurants").path("-1").build()
            .toString();

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

    @When("I send a \"delete restaurant\" request with wrong account")
    public void sendADeleteRestaurantRequestWithWrongAccount() {
        final String format = preferredFormat;
        final String language = preferredLanguage;
        response = MidipascherClient.deleteEntity(INVALID_ACCOUNT_DELETE_URI, format, language);
    }

    @When("I send a \"delete restaurant\" request with wrong id \"<wrong_id>\"")
    public void sendADeleteRestaurantRequestWithWrongId(@Named("wrong_id") final Long id) {
        final String format = preferredFormat;
        final String language = preferredLanguage;
        response = MidipascherClient.deleteEntity(INVALID_RESTAURANT_DELETE_URI, format, language);
    }

    @When("I send a valid \"delete restaurant\" request")
    public void sendAValidcreateRestaurantRequest() {
        final String format = "application/json";
        final String language = "fr";
        response = MidipascherClient.deleteEntity(DELETE_URI, format, language);
    }

}
