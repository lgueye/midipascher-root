/*
 *
 */
package fr.midipascher.stories.steps;

import java.net.URI;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class RequestingProtectedResourceWithWrongPasswordShouldFail {

    @Autowired
    @Qualifier("baseEndPoint")
    String baseEndPoint;
    URI uri;
    FoodSpecialty body;
    String requestContentType;
    String language;
    ClientConfig cc;
    Client jerseyClient;
    ClientResponse response;

    public RequestingProtectedResourceWithWrongPasswordShouldFail() {

        final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
        jerseyClient = ApacheHttpClient4.create(config);
        jerseyClient.addFilter(new LoggingFilter());
        config.getClasses().add(JacksonJsonProvider.class);
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

    }

    @Given("I authenticate with $uid uid and <password> password")
    public void authenticate(@Named("uid") final String uid, @Named("password") final String password) {
        jerseyClient.addFilter(new HTTPBasicAuthFilter(uid, password));
    }

    @Then("the error message should be <message>")
    public void expectMessage(@Named("message") final String message) {
        Assert.assertEquals(message, response.getEntity(String.class).trim());
    }

    @Then("the response code should be $statusCode")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, response.getStatus());
    }

    @Given("I send <requestContentType>")
    public void provideRequestContentType(@Named("requestContentType") final String requestContentType) {
        this.requestContentType = requestContentType;
    }

    @Given("I accept <language> language")
    public void provideresponseAcceptLanguage(@Named("language") final String language) {
        this.language = language;
    }

    @Given("I provide a valid create food specialty request body")
    public void provideValidRequestBody() {
        body = TestUtils.validFoodSpecialty();
    }

    @Then("I should get an unsuccessfull response")
    public void responseShouldBeUnsuccessfull() {
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatus());
        final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(response.getStatus()).substring(0, 1));
        Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
    }

    @When("I send a create food specialty request")
    public void sendRequest() {
        uri = URI.create(baseEndPoint + "/foodspecialty");
        final WebResource webResource = jerseyClient.resource(uri);
        response = webResource.acceptLanguage(new String[] { language }).header("Content-Type", requestContentType)
                .post(ClientResponse.class, body);
    }

}