/**
 * 
 */
package fr.midipascher.stories.steps;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.AfterScenario;
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
public class LockAccountSteps {

    private ClientResponse response;
    private String responseLanguage;
    private String responseContentType;

    // private static final String LOCK_URI = WebConstants.BACKEND_PATH + AccountsResource.COLLECTION_RESOURCE_PATH
    // + "/5/lock";

    private static final String LOCK_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("5").path("lock").build().toString();

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

    @Then("the response code should be \"$statusCode\"")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, response.getStatus());
    }

    @When("I send a \"lock account\" request with wrong id \"<wrong_id>\"")
    public void sendLockAccountRequestWithWrongId(@Named("wrong_id") final Long id) {
        final String requestContentType = "application/json";
        final String relativePath = WebConstants.BACKEND_PATH + AccountsResource.COLLECTION_RESOURCE_PATH + "/" + id
            + "/lock";
        response = MidipascherClient
                .lockEntity(relativePath, requestContentType, responseContentType, responseLanguage);
    }

    @When("I send a valid \"lock account\" request")
    public void sendValidLockAccountRequest() {
        final String requestContentType = "application/json";
        response = MidipascherClient.lockEntity(LOCK_URI, requestContentType, responseContentType, responseLanguage);
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
