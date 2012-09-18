/**
 *
 */
package fr.midipascher.steps.backend;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.ResponseError;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.AccountsResource;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.UriBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public class UpdateAccountSteps extends BackendBaseSteps {

    private static final String UPDATE_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(AccountsResource.COLLECTION_RESOURCE_PATH).path("4").build().toString();

    public UpdateAccountSteps(Exchange exchange) {
        super(exchange);
    }

    @Then("the message should be \"<message>\"")
    public void expectedMessage(@Named("message") final String message) {
        this.exchange.assertExpectedMessage(ResponseError.class, message);
    }

    @When("I send a \"update account\" request with wrong first name \"<wrong_first_name>\"")
    public void sendUpdateAccountRequestWithWrongFirstName(
            @Named("wrong_first_name") final String firstName) {
        final Account account = TestFixtures.validAccount();
        account.setFirstName(firstName);
        account.setAuthorities(null);
        account.setEmail(this.exchange.getRequest().getUid());
        this.exchange.getRequest().setBody(account);
        this.exchange.getRequest().setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

    @When("I send a \"update account\" request with wrong last name \"<wrong_last_name>\"")
    public void sendUpdateAccountRequestWithWrongLastName(
            @Named("wrong_last_name") final String lastName) {
        final Account account = TestFixtures.validAccount();
        account.setLastName(lastName);
        account.setAuthorities(null);
        account.setEmail(this.exchange.getRequest().getUid());
        this.exchange.getRequest().setBody(account);
        this.exchange.getRequest().setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

    @When("I send a \"update account\" request with wrong password \"<wrong_password>\"")
    public void sendUpdateAccountRequestWithWrongPassword(
            @Named("wrong_password") final String password) {
        final Account account = TestFixtures.validAccount();
        account.setPassword(password);
        account.setAuthorities(null);
        account.setEmail(this.exchange.getRequest().getUid());
        this.exchange.getRequest().setBody(account);
        this.exchange.getRequest().setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

    @When("I send a \"update account\" request with wrong id \"<wrong_id>\"")
    public void sendUpdateAccountRequestWithWrongUID(@Named("wrong_id") final Long id) {
        final Account account = TestFixtures.validAccount();
        account.setAuthorities(null);
        account.setEmail(this.exchange.getRequest().getUid());
        final
        String
                uri =
                WebConstants.BACKEND_PATH + AccountsResource.COLLECTION_RESOURCE_PATH + "/" + id;
        this.exchange.getRequest().setBody(account);
        this.exchange.getRequest().setUri(uri);
        this.exchange.updateEntity();


    }

    @When("I send a valid \"update account\" request")
    public void sendValidUpdateAccountRequest() {
        final Account account = TestFixtures.validAccount();
        account.setAuthorities(null);
        account.setEmail(this.exchange.getRequest().getUid());
        this.exchange.getRequest().setBody(account);
        this.exchange.getRequest().setUri(UPDATE_URI);
        this.exchange.updateEntity();
    }

}
