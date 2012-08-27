package fr.midipascher.steps.backend;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;

/**
 * User: louis.gueye@gmail.com
 */
public abstract class BackendBaseSteps {

	protected Exchange	exchange;

	protected BackendBaseSteps(Exchange exchange) {
		this.exchange = exchange;
	}

	public Exchange getExchange() {
		return this.exchange;
	}

	@Then("the response code should be \"$statusCode\"")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		this.exchange.assertExpectedStatus(statusCode);
	}

	@Then("the response message should be \"<message>\"")
	public void expectResponseMessage(@Named("message") final String responseMessage) {
		this.exchange.assertExpectedMessage(String.class, responseMessage);
	}

	@Given("I authenticate with \"<uid>\" uid and \"<password>\" password")
	public void authenticate(@Named("uid") final String uid, @Named("password") final String password) {
		this.exchange.setCredentials(uid, password);
	}

	@Given("I accept \"<responseLanguage>\" language")
	public void setAcceptLanguage(@Named("responseLanguage") final String requestedLanguage) {
		this.exchange.getRequest().setRequestedLanguage(requestedLanguage);
	}

}
