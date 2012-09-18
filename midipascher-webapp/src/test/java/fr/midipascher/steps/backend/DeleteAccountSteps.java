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
public class DeleteAccountSteps extends BackendBaseSteps {

    private static final String DELETE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("6").build()
            .toString();

    /**
     * @param exchange
     */
    public DeleteAccountSteps(Exchange exchange) {
        super(exchange);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        this.exchange.assertExpectedMessage(ResponseError.class, message);
    }

    @When("I send a \"delete account\" request with wrong id \"<wrong_id>\"")
    public void sendADeleteAccountRequestWithWrongId(@Named("wrong_id") final Long id) {
        String uri = UriBuilder.fromPath(WebConstants.BACKEND_PATH).path(AccountsResource.COLLECTION_RESOURCE_PATH)
                .path(String.valueOf(id)).build().toString();
        this.exchange.getRequest().setUri(uri);
        this.exchange.deleteEntity();
    }

    @When("I send a valid \"delete account\" request")
    public void sendAValidcreateAccountRequest() {
        this.exchange.getRequest().setUri(DELETE_URI);
        this.exchange.deleteEntity();
    }

}
