/**
 * 
 */
package fr.midipascher.steps.backend;

import javax.ws.rs.core.UriBuilder;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestFixtures;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateFoodSpecialtySteps extends BackendBaseSteps {

	private static final String	CREATE_URI	= UriBuilder.fromPath(WebConstants.BACKEND_PATH)
													.path(FoodSpecialtiesResource.class).build().toString();

	/**
	 * @param exchange
	 */
	public CreateFoodSpecialtySteps(Exchange exchange) {
		super(exchange);
	}

	@Then("I should be able to read the new resource")
	public void expectedNewResource() {
		this.exchange.assertNewResource(FoodSpecialty.class);
	}

	@When("I send a \"create food specialty\" request with wrong code \"<wrong_code>\"")
	public void sendCreateFoodSpecialtyRequestWithWrongCode(@Named("wrong_code") final String code) {
		final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
		foodSpecialty.setCode(code);
		this.exchange.getRequest().setBody(foodSpecialty);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a \"create food specialty\" request with wrong label \"<wrong_label>\"")
	public void sendCreateFoodSpecialtyRequestWithWrongLabel(@Named("wrong_label") final String label) {
		final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
		foodSpecialty.setLabel(label);
		this.exchange.getRequest().setBody(foodSpecialty);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

	@When("I send a valid \"create food specialty\" request")
	public void sendValidCreateFoodSpecialtyRequest() {
		final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
		this.exchange.getRequest().setBody(foodSpecialty);
		this.exchange.getRequest().setUri(CREATE_URI);
		this.exchange.createEntity();
	}

}
