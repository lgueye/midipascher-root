/**
 * 
 */
package fr.midipascher.steps.backend;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

/**
 * @author louis.gueye@gmail.com
 */
public class PagesStatusSteps {

    private ClientResponse response;

    @When("I send a \"<http_method>\" request at uri \"<uri>\"")
    public void sendRequest(@Named("http_method") final String httpMethod, @Named("uri") final String uri) {
        this.response = MidipascherClient.readURI(uri, "text/html", "fr");
    }

    @Then("the response code should be \"$statusCode\"")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, this.response.getStatus());
    }
}
