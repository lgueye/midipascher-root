/*
 *
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.AfterStory;
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
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthenticationSteps {

	private final String		baseEndPoint		= ResourceBundle.getBundle("stories-context").getString(
															"baseEndPoint");
	private final List<String>	resources			= new ArrayList<String>();
	private String				responseLanguage	= "en";
	private ClientResponse		response;
	private final Client		jerseyClient;

	public AuthenticationSteps() {
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		this.jerseyClient = ApacheHttpClient4.create(config);
		this.jerseyClient.addFilter(new LoggingFilter());
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
	}

	@Given("I authenticate with <uid> uid and $password password")
	@Aliases(values = { "I authenticate with $uid uid and <password> password",
			"I authenticate with $uid uid and $password password" })
	public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
		this.jerseyClient.addFilter(new HTTPBasicAuthFilter(uid, password));
	}

	@Given("I accept <responseLanguage> language")
	public void setAcceptLanguage(@Named("responseLanguage") final String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}

	@When("I request a protected resource")
	public void requestProtectedResource() {
		final String path = "/foodspecialty/protected";
		final URI uri = URI.create(this.baseEndPoint + path);
		this.response = this.jerseyClient.resource(uri).acceptLanguage(new String[] { this.responseLanguage })
				.get(ClientResponse.class);
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
		String error = this.response.getEntity(String.class);
		Assert.assertEquals(responseMessage, error.trim());
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
