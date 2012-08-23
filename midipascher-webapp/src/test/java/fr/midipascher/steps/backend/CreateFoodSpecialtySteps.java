/**
 * 
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import fr.midipascher.test.TestFixtures;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.steps.MidipascherClient;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateFoodSpecialtySteps {

    private ClientResponse response;
    private String responseLanguage;
    private String responseContentType;

    private static final String CREATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(FoodSpecialtiesResource.class).build().toString();

    @AfterScenario
    public void afterScenario() {
        responseLanguage = null;
        responseContentType = null;
    }

    @Given("I authenticate with \"$uid\" uid and \"$password\" password")
    public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
        MidipascherClient.setCredentials(uid, password);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        Assert.assertEquals(message, response.getEntity(ResponseError.class).getMessage());
    }

    @Then("I should be able to read the new resource")
    public void expectedNewResource() {
        final String responseFormat = responseContentType;
        final String responseLanguage = "en";
        response = MidipascherClient.readLocation(response.getLocation(), responseFormat, responseLanguage);
        Assert.assertEquals(200, response.getStatus());
        Assert.assertNotNull(response.getEntity(FoodSpecialty.class));
    }

    @Then("the response code should be \"$statusCode\"")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, response.getStatus());
    }

    @When("I send a \"create food specialty\" request with wrong code \"<wrong_code>\"")
    public void sendCreateFoodSpecialtyRequestWithWrongCode(@Named("wrong_code") final String code) {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        foodSpecialty.setCode(code);
        final String requestContentType = "application/json";
        response = MidipascherClient.createEntity(foodSpecialty, CREATE_URI, requestContentType, responseContentType,
            responseLanguage);
    }

    @When("I send a \"create food specialty\" request with wrong label \"<wrong_label>\"")
    public void sendCreateFoodSpecialtyRequestWithWrongLabel(@Named("wrong_label") final String label) {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        foodSpecialty.setLabel(label);
        final String requestContentType = "application/json";
        response = MidipascherClient.createEntity(foodSpecialty, CREATE_URI, requestContentType, responseContentType,
            responseLanguage);
    }

    @When("I send a valid \"create food specialty\" request")
    public void sendValidCreateFoodSpecialtyRequest() {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        final String requestContentType = "application/json";
        response = MidipascherClient.createEntity(foodSpecialty, CREATE_URI, requestContentType, responseContentType,
            responseLanguage);
    }

    @Given("I accept \"$responseContentType\" format")
    public void setFormat(@Named("responseContentType") final String responseContentType) {
        this.responseContentType = responseContentType;
    }

    @Given("I accept \"$responseLanguage\" language")
    public void setLanguage(@Named("responseLanguage") final String responseLanguage) {
        this.responseLanguage = responseLanguage;
    }
}