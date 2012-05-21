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
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class UpdateAccountSteps {

	private ClientResponse	response;
	private String			language;
	private String			format;
	private final String	relativePath	= "/account/3";
	private String			uid;

	@Then("the message should be \"<message>\"")
	public void expectedMessage(@Named("message") final String message) {
		Assert.assertEquals(message, this.response.getEntity(ResponseError.class).getMessage());
	}

	@Given("I provide \"$uid\" uid and \"$password\" password")
	public void provideAuthInformations(@Named("uid") String uid, @Named("password") String password) {
		this.uid = uid;
		MidipascherClient.setCredentials(uid, password);
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@When("I send a \"update account\" request with wrong first name \"<wrong_first_name>\"")
	public void sendUpdateAccountRequestWithWrongFirstName(@Named("wrong_first_name") final String firstName) {
		final Account account = TestUtils.validAccount();
		account.setFirstName(firstName);
		account.setAuthorities(null);
		account.setEmail(this.uid);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		this.response = MidipascherClient
				.updateEntity(account, this.relativePath, requestContentType, format, language);
	}

	@When("I send a \"update account\" request with wrong last name \"<wrong_last_name>\"")
	public void sendUpdateAccountRequestWithWrongLastName(@Named("wrong_last_name") final String lastName) {
		final Account account = TestUtils.validAccount();
		account.setLastName(lastName);
		account.setAuthorities(null);
		account.setEmail(this.uid);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		this.response = MidipascherClient
				.updateEntity(account, this.relativePath, requestContentType, format, language);
	}

	@When("I send a \"update account\" request with wrong id \"<wrong_id>\"")
	public void sendUpdateAccountRequestWithWrongUID(@Named("wrong_id") final Long id) {
		final Account account = TestUtils.validAccount();
		account.setAuthorities(null);
		account.setEmail(this.uid);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		String relativePath = "/account/" + id;
		this.response = MidipascherClient.updateEntity(account, relativePath, requestContentType, format, language);
	}

	@When("I send a \"update account\" request with wrong password \"<wrong_password>\"")
	public void sendUpdateAccountRequestWithWrongPassword(@Named("wrong_password") final String password) {
		final Account account = TestUtils.validAccount();
		account.setPassword(password);
		account.setAuthorities(null);
		account.setEmail(this.uid);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		this.response = MidipascherClient
				.updateEntity(account, this.relativePath, requestContentType, format, language);
	}

	@When("I send a valid \"update account\" request")
	public void sendValidUpdateAccountRequest() {
		final Account account = TestUtils.validAccount();
		account.setAuthorities(null);
		account.setEmail(this.uid);
		final String requestContentType = "application/json";
		final String language = this.language;
		final String format = this.format;
		this.response = MidipascherClient
				.updateEntity(account, this.relativePath, requestContentType, format, language);
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
