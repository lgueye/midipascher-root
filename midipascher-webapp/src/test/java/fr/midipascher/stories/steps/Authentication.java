/*
 *
 */
package fr.midipascher.stories.steps;

import java.net.URI;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class Authentication {

	private final HttpHeaders			headers	= new HttpHeaders();
	@Autowired
	@Qualifier("baseEndPoint")
	private String						baseEndPoint;
	private URI							uri;
	private FoodSpecialty				body;
	private UsernamePasswordCredentials	credentials;
	private HttpStatusCodeException		responseException;

	@Autowired
	private RestTemplate				restTemplate;

	@Given("I authenticate with <uid> uid and $password password")
	public void authenticate(@Named("uid") final String uid, @Named("password") final String password) {
		this.credentials = new UsernamePasswordCredentials(uid, password);
	}

	@Given("I provide a valid create food specialty request body")
	public void provideValidRequestBody() {
		this.body = TestUtils.validFoodSpecialty();
	}

	@Given("I send <request-contenttype>")
	public void provideRequestContentType(@Named("request-contenttype") final String requestContentType) {
		this.headers.setContentType(MediaType.valueOf(requestContentType));
	}

	@When("I send a create food specialty request")
	public void sendRequest() {
		((CommonsClientHttpRequestFactory) this.restTemplate.getRequestFactory()).getHttpClient().getState()
				.setCredentials(new AuthScope("localhost", 9080, AuthScope.ANY_REALM), this.credentials);
		String endPoint = this.baseEndPoint + "/foodspecialty";
		this.uri = URI.create(endPoint);
		final HttpEntity<FoodSpecialty> requestEntity = new HttpEntity<FoodSpecialty>(this.body, this.headers);
		try {
			this.restTemplate.exchange(this.uri, HttpMethod.POST, requestEntity, String.class);
			Assert.fail("HttpStatusCodeException expected");
		} catch (HttpStatusCodeException e) {
			this.responseException = e;
		}
	}

	@Then("I should get an unsuccessfull response")
	public void responseShouldBeUnsuccessfull() {
		Assert.assertNotNull(this.responseException);
		int statusCodeFirstDigit = Integer.valueOf(String.valueOf(this.responseException.getStatusCode().value())
				.substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
	}

	@Then("the response code should be $statusCode")
	public void expectStatusCode(int statusCode) {
		// if (this.responseException != null) {
		// System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " +
		// this.responseException.getMessage());
		// System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "
		// + this.responseException.getMostSpecificCause());
		// this.responseException.printStackTrace();
		// }
		Assert.assertEquals(statusCode, this.responseException.getStatusCode().value());

	}
}
