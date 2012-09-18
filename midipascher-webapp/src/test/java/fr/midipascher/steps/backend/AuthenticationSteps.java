/*
 *
 */
package fr.midipascher.steps.backend;

import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AuthoritiesResource;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthenticationSteps extends BackendBaseSteps {

    /**
     * @param exchange
     */
    public AuthenticationSteps(Exchange exchange) {
        super(exchange);
    }

    private static final String URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH).path(AuthoritiesResource.class)
            .build().toString();

    @When("I request a protected resource")
    public void requestProtectedResource() {
        this.getExchange().getRequest().setUri(URI);
        this.getExchange().getRequest().setRequestedType("application/json");
        this.getExchange().readURI();
    }

    @Then("the response message should be \"<message>\"")
    public void expectResponseMessage(@Named("message") final String responseMessage) {
        this.exchange.assertExpectedMessage(String.class, responseMessage);
    }

}
