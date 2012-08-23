/*
 *
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import fr.midipascher.test.TestFixtures;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.steps.MidipascherClient;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class ContentNegotiationSteps {

    private String responseContentType;
    private String requestContentType;
    private ClientResponse response;

    private static final String CREATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(FoodSpecialtiesResource.class).build().toString();

    private static final String SEARCH_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(FoodSpecialtiesResource.class).path("search").build().toString();

    @Then("the response code should be \"$statusCode\"")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, response.getStatus());
    }

    @Then("I should get my newly created resource")
    public void getResourceAtLocation() {
        final String responseFormat = responseContentType;
        final String responseLanguage = "en";
        response = MidipascherClient.readLocation(response.getLocation(), responseFormat, responseLanguage);
        Assert.assertNotNull(response.getEntity(FoodSpecialty.class));
    }

    @When("I send a create request")
    public void sendCreateRequest() {
        final String requestFormat = requestContentType;
        final String responseFormat = "application/json";
        final String responseLanguage = "en";
        MidipascherClient.setCredentials("admin@admin.com", "secret");
        response = MidipascherClient.createEntity(TestFixtures.validFoodSpecialty(), CREATE_URI, requestFormat,
            responseFormat, responseLanguage);
    }

    @When("I send a search request")
    public void sendSearchRequest() {
        final String requestContentType = "application/x-www-form-urlencoded";
        final String queryString = "";
        response = MidipascherClient.findEntityByCriteria(SEARCH_URI, queryString, requestContentType,
            responseContentType, "en");
    }

    @Given("I send \"<requestContentType>\" data")
    public void setRequestContentType(@Named("requestContentType") final String requestContentType) {
        this.requestContentType = requestContentType;
    }

    @Given("I receive \"<responseContentType>\" data")
    public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
        this.responseContentType = responseContentType;
    }

}
