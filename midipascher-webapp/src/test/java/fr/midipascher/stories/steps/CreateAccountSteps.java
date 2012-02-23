/**
 * 
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
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

import fr.midipascher.domain.Account;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateAccountSteps {

	private final String		baseEndPoint	= ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
	private ClientResponse		response;
	private String				language;
	private String				format;
	private final List<String>	resources		= new ArrayList<String>();

	@BeforeStory
	public void beforeStory() {
		TestUtils.createAuthority();
	}

	@Then("the message should be \"<message>\"")
	public void expectedMessage(@Named("message") final String message) {
		Assert.assertEquals(message, this.response.getEntity(ResponseError.class).getMessage());
	}

	@Then("I should be able to read the new resource")
	public void expectedNewResource() {
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		// jerseyClient.addFilter(new HTTPBasicAuthFilter(this.uid,
		// this.password));
		final WebResource webResource = jerseyClient.resource(this.response.getLocation());
		this.response = webResource.accept(MediaType.valueOf("application/json")).acceptLanguage(new String[] { "fr" })
				.get(ClientResponse.class);
		Assert.assertEquals(200, this.response.getStatus());
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@When("I send a \"create account\" request with wrong first name \"<wrong_first_name>\"")
	public void sendCreateUserRequestWithWrongFirstName(@Named("wrong_first_name") final String firstName) {
		final Account account = TestUtils.validUser();
		account.setFirstName(firstName);
		final String path = "/accounts";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = this.format;
		final String language = this.language;
		this.response = webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, account);
	}

	@When("I send a \"create account\" request with wrong last name \"<wrong_last_name>\"")
	public void sendCreateUserRequestWithWrongLastName(@Named("wrong_last_name") final String lastName) {
		final Account account = TestUtils.validUser();
		account.setLastName(lastName);
		final String path = "/accounts";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = this.format;
		final String language = this.language;
		this.response = webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, account);
	}

	@When("I send a \"create account\" request with wrong uid \"<wrong_uid>\"")
	public void sendCreateUserRequestWithWrongUID(@Named("wrong_uid") final String email) {
		final Account account = TestUtils.validUser();
		account.setEmail(email);
		final String path = "/accounts";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = this.format;
		final String language = this.language;
		this.response = webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, account);
	}

	@When("I send a \"create account\" request with wrong password \"<wrong_password>\"")
	public void sendCreateUserRequestWithWrongPassword(@Named("wrong_password") final String password) {
		final Account account = TestUtils.validUser();
		account.setPassword(password);
		final String path = "/accounts";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = this.format;
		final String language = this.language;
		this.response = webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, account);
	}

	@When("I send a valid \"create account\" request")
	public void sendValidCreateUserRequest() {
		final Account account = TestUtils.validUser();
		final String path = "/accounts";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = this.format == null ? "application/json" : this.format;
		final String language = this.language == null ? "en" : this.language;
		this.response = webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, account);
	}

	@Given("I accept \"$format\" format")
	public void setFormat(@Named("format") final String format) {
		this.format = format;
	}

	@Given("I accept \"$language\" language")
	public void setLanguage(@Named("language") final String language) {
		this.language = language;
	}

	@Given("existing accounts: $accounts")
	public void setup(@Named("accounts") final ExamplesTable accountsAsTable) {
		final String path = "/accounts";
		final URI uri = URI.create(this.baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = this.format == null ? "application/json" : this.format;
		final String language = this.language == null ? "en" : this.language;

		for ( final Map<String, String> row : accountsAsTable.getRows() )
			if (row != null) {
				final Account account = TestUtils.validUser();
				account.setFirstName(row.get("firstName"));
				account.setLastName(row.get("lastName"));
				account.setEmail(row.get("uid"));
				account.setPassword(row.get("password"));
				final ClientResponse response = webResource.accept(MediaType.valueOf(format))
						.acceptLanguage(new String[] { language }).header("Content-Type", requestContentType)
						.post(ClientResponse.class, account);
				this.resources.add(response.getLocation().toString());

			}

	}

	@Given("I delete existing accounts")
	public void clear() {
		this.language = null;
		this.format = null;
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter("admin", "secret"));
		for ( final String resource : this.resources ) {
			final WebResource webResource = jerseyClient.resource(resource);
			webResource.delete();
		}
		this.resources.clear();
	}
}
