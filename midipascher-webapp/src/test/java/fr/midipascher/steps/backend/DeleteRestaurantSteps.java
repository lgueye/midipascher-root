/**
 *
 */
package fr.midipascher.steps.backend;

import fr.midipascher.domain.ResponseError;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AccountsResource;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public class DeleteRestaurantSteps extends BackendBaseSteps {

    private static final String DELETE_URI = UriBuilder
            .fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH)
            .path("3").path("restaurants").path("2")
            .build().toString();

    private static final String INVALID_ACCOUNT_DELETE_URI = UriBuilder
            .fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH)
            .path("-1").path("restaurants").path("2")
            .build().toString();

    private static final String INVALID_RESTAURANT_DELETE_URI = UriBuilder
            .fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH)
            .path("3").path("restaurants").path("-1")
            .build().toString();

    /**
     * @param exchange
     */
    public DeleteRestaurantSteps(Exchange exchange) {
        super(exchange);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        this.exchange.assertExpectedMessage(ResponseError.class, message);
    }

    @When("I send a \"delete restaurant\" request with wrong account")
    public void sendADeleteRestaurantRequestWithWrongAccount() {
        this.exchange.getRequest().setUri(INVALID_ACCOUNT_DELETE_URI);
        this.exchange.deleteEntity();
    }

    @When("I send a \"delete restaurant\" request with wrong id \"<wrong_id>\"")
    public void sendADeleteRestaurantRequestWithWrongId(@Named("wrong_id") final Long id) {
        this.exchange.getRequest().setUri(INVALID_RESTAURANT_DELETE_URI);
        this.exchange.deleteEntity();
    }

    @When("I send a valid \"delete restaurant\" request")
    public void sendAValidcreateRestaurantRequest() {
        this.exchange.getRequest().setUri(DELETE_URI);
        this.exchange.deleteEntity();
    }

}
