/**
 * 
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AccountsResource;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateAccountSteps extends BackendBaseSteps {

	private static final String	CREATE_URI	= UriBuilder.fromPath(WebConstants.BACKEND_PATH)
													.path(AccountsResource.class).build().toString();

	/**
	 * @param exchange
	 */
	public CreateAccountSteps(Exchange exchange) {
		super(exchange);
	}

	@Then("the message should be \"<message>\"")
	public void expectedMessage(@Named("message") final String message) {
		this.exchange.assertExpectedMessage(ResponseError.class, message);
	}

	@Then("I should be able to read the new resource")
	public void expectedNewResource() {
		this.exchange.assertNewResource(Account.class);
	}

	@When("I send a \"create account\" request with wrong first name \"<wrong_first_name>\"")
	public void sendCreateUserRequestWithWrongFirstName(@Named("wrong_first_name") final String firstName) {
		final Account account = TestFixtures.validAccount();
		account.setFirstName(firstName);
		account.setAuthorities(null);
		this.exchange.getRequest().setBody(account);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a \"create account\" request with wrong last name \"<wrong_last_name>\"")
	public void sendCreateUserRequestWithWrongLastName(@Named("wrong_last_name") final String lastName) {
		final Account account = TestFixtures.validAccount();
		account.setLastName(lastName);
		account.setAuthorities(null);
		this.exchange.getRequest().setBody(account);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a \"create account\" request with wrong password \"<wrong_password>\"")
	public void sendCreateUserRequestWithWrongPassword(@Named("wrong_password") final String password) {
		final Account account = TestFixtures.validAccount();
		account.setPassword(password);
		account.setAuthorities(null);
		this.exchange.getRequest().setBody(account);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a \"create account\" request with wrong uid \"<wrong_uid>\"")
	public void sendCreateUserRequestWithWrongUID(@Named("wrong_uid") final String email) {
		final Account account = TestFixtures.validAccount();
		account.setEmail(email);
		account.setAuthorities(null);
		this.exchange.getRequest().setBody(account);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a valid \"create account\" request")
	public void sendValidCreateUserRequest() {
		final Account account = TestFixtures.validAccount();
		account.setAuthorities(null);
		this.exchange.getRequest().setBody(account);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

}
