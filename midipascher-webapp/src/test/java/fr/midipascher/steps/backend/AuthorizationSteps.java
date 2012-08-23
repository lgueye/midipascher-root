/*
 *
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import fr.midipascher.test.TestFixtures;
import org.jbehave.core.annotations.Alias;
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
public class AuthorizationSteps {

    private String responseLanguage;
    private ClientResponse response;
    private String responseContentType;

    private static final String URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(FoodSpecialtiesResource.class).build().toString();

    @Given("I authenticate with \"$uid\" uid and \"$password\" password")
    public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
        MidipascherClient.setCredentials(uid, password);
    }

    @Then("the response message should be \"<message>\"")
    public void expectResponseMessage(@Named("message") final String responseMessage) {
        final ResponseError error = response.getEntity(ResponseError.class);
        Assert.assertNotNull(error);
        Assert.assertNotNull(error.getMessage());
        Assert.assertEquals(responseMessage, error.getMessage().trim());
    }

    @Then("the response code should be \"$statusCode\"")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, response.getStatus());
    }

    @When("I request a protected resource that require ADMIN rights")
    @Alias("I request a protected resource")
    public void requestProtectedResource() {
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        final String path = AuthorizationSteps.URI;
        final String requestContentType = "application/json";
        response = MidipascherClient.createEntity(foodSpecialty, path, requestContentType, responseContentType,
            responseLanguage);
    }

    @Given("I accept \"<responseLanguage>\" language")
    public void setAcceptLanguage(@Named("responseLanguage") final String responseLanguage) {
        this.responseLanguage = responseLanguage;
    }

    @Given("I receive \"<responseContentType>\" data")
    public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
        this.responseContentType = responseContentType;
    }

}
