package fr.midipascher.steps;

/**
 * User: louisgueye@gmailcom Date: 26/08/12 Time: 15:47
 * 
 * @param <T>
 */
public abstract class BaseSteps {

	protected Exchange	exchange;

	protected BaseSteps(Exchange exchange) {
		this.exchange = exchange;
	}

	public Exchange getExchange() {
		return this.exchange;
	}
}
