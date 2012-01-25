/**
 * 
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
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
public class CreateFoodSpecialtySteps {

	private final String	baseEndPoint	= ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
	private String			uid;
	private String			password;
	private ClientResponse	response;

	@Given("I authenticate with \"$uid\" uid and \"$password\" password")
	public void authenticateWithWrongUid(@Named("uid") final String uid, @Named("password") final String password) {
		this.uid = uid;
		this.password = password;
	}

	@When("I send a valid \"create food specialty\" request")
	public void sendValidCreateFoodSpecialtyRequest() {
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
		this.response = webResource.accept(MediaType.valueOf("application/json")).acceptLanguage(new String[] { "fr" })
				.header("Content-Type", requestContentType).post(ClientResponse.class, foodSpecialty);
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@Then("I should be able to read the new resource")
	public void expectedNewResource() {
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter(this.uid, this.password));
		final WebResource webResource = jerseyClient.resource(this.response.getLocation());
		this.response = webResource.accept(MediaType.valueOf("application/json")).acceptLanguage(new String[] { "fr" })
				.get(ClientResponse.class);
		Assert.assertEquals(200, this.response.getStatus());
	}

}
