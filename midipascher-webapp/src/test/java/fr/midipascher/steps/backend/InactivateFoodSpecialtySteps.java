/**
 *
 */
package fr.midipascher.steps.backend;

import fr.midipascher.domain.ResponseError;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public class InactivateFoodSpecialtySteps extends BackendBaseSteps {

    private static final String INACTIVATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(FoodSpecialtiesResource.COLLECTION_RESOURCE_PATH).path("5").path("inactivate").build()
            .toString();

    public InactivateFoodSpecialtySteps(Exchange exchange) {
        super(exchange);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        this.exchange.assertExpectedMessage(ResponseError.class, message);
    }

    @When("I send a \"inactivate food specialty\" request with wrong id \"<wrong_id>\"")
    public void sendInactivateFoodSpecialtyRequestWithWrongId(@Named("wrong_id") final Long id) {
        final String uri = WebConstants.BACKEND_PATH
                + FoodSpecialtiesResource.COLLECTION_RESOURCE_PATH + "/"
                + id + "/inactivate";
        this.exchange.getRequest().setUri(uri);
        this.exchange.inactivateEntity();

    }

    @When("I send a valid \"inactivate food specialty\" request")
    public void sendValidInactivateFoodSpecialtyRequest() {
        this.exchange.getRequest().setUri(INACTIVATE_URI);
        this.exchange.inactivateEntity();
    }

}
