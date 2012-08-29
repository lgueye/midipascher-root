/**
 *
 */
package fr.midipascher.steps.backend;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

import fr.midipascher.domain.ResponseError;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AuthoritiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class InactivateAuthoritySteps extends BackendBaseSteps {

  private static final String INACTIVATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
      .path(AuthoritiesResource.COLLECTION_RESOURCE_PATH).path("3").path("inactivate").build()
      .toString();

  public InactivateAuthoritySteps(Exchange exchange) {
    super(exchange);
  }

  @Then("the message should be \"<message>\"")
  public void expectedMessage(@Named("message") final String message) {
    this.exchange.assertExpectedMessage(ResponseError.class, message);
  }

  @When("I send a \"inactivate authority\" request with wrong id \"<wrong_id>\"")
  public void sendInactivateAuthorityRequestWithWrongId(@Named("wrong_id") final Long id) {
    final
    String
        uri =
        WebConstants.BACKEND_PATH + AuthoritiesResource.COLLECTION_RESOURCE_PATH + "/" + id
        + "/inactivate";
    this.exchange.getRequest().setUri(uri);
    this.exchange.inactivateEntity();
  }

  @When("I send a valid \"inactivate authority\" request")
  public void sendValidInactivateAuthorityRequest() {
    this.exchange.getRequest().setUri(INACTIVATE_URI);
    this.exchange.inactivateEntity();
  }

}
