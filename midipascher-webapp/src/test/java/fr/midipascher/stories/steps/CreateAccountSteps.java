/**
 * 
 */
package fr.midipascher.stories.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateAccountSteps {

	private ClientResponse	response;
	private String			language;
	private String			format;

	@Then("the message should be \"<message>\"")
	public void expectedMessage(@Named("message") final String message) {
		Assert.assertEquals(message, this.response.getEntity(ResponseError.class).getMessage());
	}

	@Then("I should be able to read the new resource")
	public void expectedNewResource() {
		String responseFormat = "application/json";
		String responseLanguage = "en";
		this.response = MidipascherClient.readLocation(this.response.getLocation(), responseFormat, responseLanguage);
		Assert.assertEquals(200, this.response.getStatus());
		Assert.assertNotNull(this.response.getEntity(FoodSpecialty.class));
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@When("I send a \"create account\" request with wrong first name \"<wrong_first_name>\"")
	public void sendCreateUserRequestWithWrongFirstName(@Named("wrong_first_name") final String firstName) {
		final Account account = TestUtils.validUser();
		account.setFirstName(firstName);
		account.setAuthorities(null);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		String relativePath = "/accounts";
		this.response = MidipascherClient.createEntity(account, relativePath, requestContentType, format, language);
	}

	@When("I send a \"create account\" request with wrong last name \"<wrong_last_name>\"")
	public void sendCreateUserRequestWithWrongLastName(@Named("wrong_last_name") final String lastName) {
		final Account account = TestUtils.validUser();
		account.setLastName(lastName);
		account.setAuthorities(null);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		String relativePath = "/accounts";
		this.response = MidipascherClient.createEntity(account, relativePath, requestContentType, format, language);
	}

	@When("I send a \"create account\" request with wrong uid \"<wrong_uid>\"")
	public void sendCreateUserRequestWithWrongUID(@Named("wrong_uid") final String email) {
		final Account account = TestUtils.validUser();
		account.setEmail(email);
		account.setAuthorities(null);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		String relativePath = "/accounts";
		this.response = MidipascherClient.createEntity(account, relativePath, requestContentType, format, language);
	}

	@When("I send a \"create account\" request with wrong password \"<wrong_password>\"")
	public void sendCreateUserRequestWithWrongPassword(@Named("wrong_password") final String password) {
		final Account account = TestUtils.validUser();
		account.setPassword(password);
		account.setAuthorities(null);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		String relativePath = "/accounts";
		this.response = MidipascherClient.createEntity(account, relativePath, requestContentType, format, language);
	}

	@When("I send a valid \"create account\" request")
	public void sendValidCreateUserRequest() {
		final Account account = TestUtils.validUser();
		account.setAuthorities(null);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		String relativePath = "/accounts";
		this.response = MidipascherClient.createEntity(account, relativePath, requestContentType, format, language);
	}

	@Given("I accept \"$format\" format")
	public void setFormat(@Named("format") final String format) {
		this.format = format;
	}

	@Given("I accept \"$language\" language")
	public void setLanguage(@Named("language") final String language) {
		this.language = language;
	}

}
