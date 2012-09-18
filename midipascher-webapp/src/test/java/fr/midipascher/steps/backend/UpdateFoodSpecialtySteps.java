/**
 *
 */
package fr.midipascher.steps.backend;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public class UpdateFoodSpecialtySteps extends BackendBaseSteps {

    private static final String UPDATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(FoodSpecialtiesResource.COLLECTION_RESOURCE_PATH).path("5").build().toString();

    public UpdateFoodSpecialtySteps(Exchange exchange) {
        super(exchange);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        this.exchange.assertExpectedMessage(ResponseError.class, message);
    }

    @When("I send a \"update food specialty\" request with wrong code \"<wrong_code>\"")
    public void sendUpdateFoodSpecialtyRequestWithWrongCode(@Named("wrong_code") final String code) {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        foodSpecialty.setCode(code);
        Request request = this.exchange.getRequest();
        request.setBody(foodSpecialty);
        request.setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

    @When("I send a \"update food specialty\" request with wrong id \"<wrong_id>\"")
    public void sendUpdateFoodSpecialtyRequestWithWrongId(@Named("wrong_id") final Long id) {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        Request request = this.exchange.getRequest();
        request.setBody(foodSpecialty);
        final String relativePath = WebConstants.BACKEND_PATH + FoodSpecialtiesResource.COLLECTION_RESOURCE_PATH + "/"
                + id;
        request.setUri(relativePath);
        this.exchange.updateEntity();
    }

    @When("I send a \"update food specialty\" request with wrong label \"<wrong_label>\"")
    public void sendUpdateFoodSpecialtyRequestWithWrongLabel(@Named("wrong_label") final String label) {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        foodSpecialty.setLabel(label);
        Request request = this.exchange.getRequest();
        request.setBody(foodSpecialty);
        request.setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

    @When("I send a valid \"update food specialty\" request")
    public void sendValidUpdateFoodSpecialtyRequest() {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        Request request = this.exchange.getRequest();
        request.setBody(foodSpecialty);
        request.setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

}
