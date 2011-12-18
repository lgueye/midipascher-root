/*
 *
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthorizationSteps {

	private final String		baseEndPoint		= ResourceBundle.getBundle("stories-context").getString(
															"baseEndPoint");
	private final List<String>	resources			= new ArrayList<String>();
	private String				responseLanguage	= "en";
	private ClientResponse		response;
	private String				responseContentType;
	private String				uid;
	private String				password;

	@Given("I authenticate with <uid> uid and $password password")
	@Aliases(values = { "I authenticate with $uid uid and <password> password",
			"I authenticate with $uid uid and $password password" })
	public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
		this.uid = uid;
		this.password = password;
	}

	@Given("I accept <responseLanguage> language")
	public void setAcceptLanguage(@Named("responseLanguage") final String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}

	@When("I request a protected resource that require ADMIN rights")
	@Alias("I request a protected resource")
	public void requestProtectedResource() {
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		final String path = "/foodspecialty";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter(this.uid, this.password));
		final WebResource webResource = jerseyClient.resource(uri);
		this.response = webResource.accept(MediaType.valueOf(this.responseContentType))
				.acceptLanguage(new String[] { this.responseLanguage }).header("Content-Type", requestContentType)
				.post(ClientResponse.class, foodSpecialty);
	}

	@Given("I receive <responseContentType> data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

	@Then("I should get an unsuccessful response")
	public void responseShouldBeUnsuccessful() {
		int responseStatus = this.response.getStatus();
		final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(responseStatus).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
	}

	@Then("the response code should be $statusCode")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@Then("the response message should be <message>")
	public void expectResponseMessage(@Named("message") final String responseMessage) {
		ResponseError error = this.response.getEntity(ResponseError.class);
		Assert.assertNotNull(error);
		Assert.assertNotNull(error.getMessage());
		Assert.assertEquals(responseMessage, error.getMessage().trim());
	}

	@Then("I should get a successful response")
	public void responseShouldBeSuccessful() {
		int responseStatus = this.response.getStatus();
		final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(responseStatus).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit == 2);
	}

	@BeforeStory
	public void setup() {

		this.resources.clear();

		for (int i = 0; i < 2; i++) {

			final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
			final String path = "/foodspecialty";
			final URI uri = URI.create(this.baseEndPoint + path);
			final String requestContentType = "application/json";
			final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
			config.getClasses().add(JacksonJsonProvider.class);
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			final Client jerseyClient = ApacheHttpClient4.create(config);
			final WebResource webResource = jerseyClient.resource(uri);
			final ClientResponse response = webResource.header("Content-Type", requestContentType).post(
					ClientResponse.class, foodSpecialty);

			if (response.getLocation() != null) this.resources.add(response.getLocation().toString());

		}

	}

	@AfterStory
	public void tearDown() {
		for (final String resource : this.resources) {
			final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
			final Client jerseyClient = ApacheHttpClient4.create(config);
			final WebResource webResource = jerseyClient.resource(resource);
			webResource.delete();
		}
	}

}
