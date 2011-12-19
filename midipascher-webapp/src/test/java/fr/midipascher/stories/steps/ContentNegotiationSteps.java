/*
 *
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.AfterStory;
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
public class ContentNegotiationSteps {

	private final String		baseEndPoint	= ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
	private String				responseContentType;
	private final List<String>	resources		= new ArrayList<String>();
	private int					responseStatus;
	private String				requestContentType;
	private String				lastCreatedResourceURI;

	@Then("the response code should be $statusCode")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.responseStatus);
	}

	@Then("I should get my newly created resource")
	public void getResourceAtLocation() {
		final URI uri = URI.create(this.lastCreatedResourceURI);
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);
		final FoodSpecialty foodSpecialty = webResource.accept("application/json").get(FoodSpecialty.class);
		Assert.assertNotNull(foodSpecialty);
	}

	@Then("I should get a successful response")
	public void getResponse() {
		Assert.assertNotNull(this.responseStatus);
		final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(this.responseStatus).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit == 2);
	}

	@Then("I should get an unsuccessful response")
	public void responseShouldBeUnsuccessful() {
		Assert.assertNotNull(this.responseStatus);
		final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(this.responseStatus).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
	}

	@When("I send a create request")
	public void sendCreateRequest() {
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		final String path = "/foodspecialty";
		final URI uri = URI.create(this.baseEndPoint + path);
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter("admin", "secret"));
		final WebResource webResource = jerseyClient.resource(uri);
		jerseyClient.addFilter(new LoggingFilter());
		final ClientResponse response = webResource.header("Content-Type", this.requestContentType).post(
				ClientResponse.class, foodSpecialty);
		this.responseStatus = response.getStatus();
		if (response.getLocation() != null && StringUtils.isNotEmpty(response.getLocation().toString())) this.lastCreatedResourceURI = response
				.getLocation().toString();
	}

	@When("I send a search request")
	public void sendSearchRequest() {
		final StringBuilder queryBuilder = new StringBuilder();
		final String path = "/foodspecialty/find";
		final String query = queryBuilder.toString();
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/x-www-form-urlencoded";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final ClientResponse response = jerseyClient.resource(uri).accept(MediaType.valueOf(this.responseContentType))
				.header("Content-Type", requestContentType).post(ClientResponse.class, query);
		this.responseStatus = response.getStatus();
	}

	@Given("I send <requestContentType> data")
	public void setRequestContentType(@Named("requestContentType") final String requestContentType) {
		this.requestContentType = requestContentType;
	}

	@Given("I receive <responseContentType> data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

	@BeforeStory
	public void setup() {

		this.resources.clear();

		for (int i = 0; i < 10; i++) {

			final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
			final String path = "/foodspecialty";
			final URI uri = URI.create(this.baseEndPoint + path);
			final String requestContentType = "application/json";
			final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
			config.getClasses().add(JacksonJsonProvider.class);
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			final Client jerseyClient = ApacheHttpClient4.create(config);
			jerseyClient.addFilter(new LoggingFilter());
			jerseyClient.addFilter(new HTTPBasicAuthFilter("admin", "secret"));
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
