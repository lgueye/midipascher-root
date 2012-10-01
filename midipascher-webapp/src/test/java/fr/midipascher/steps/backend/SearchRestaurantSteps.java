package fr.midipascher.steps.backend;

import fr.midipascher.domain.Restaurant;
import fr.midipascher.steps.backend.Exchange;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;
import fr.midipascher.web.resources.SearchRestaurantsResource;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Pending;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public class SearchRestaurantSteps extends BackendBaseSteps {

    private static final String SEARCH_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(SearchRestaurantsResource.class).build().toString();

    public SearchRestaurantSteps(Exchange exchange) {
        super(exchange);
    }

    @Given("I search for restaurants which name matches \"$name\"")
    public void searchRestaurantByName(@Named("name") String name) {
        Restaurant criteria = new Restaurant();
        criteria.setName(name);
        this.exchange.getRequest().setBody(criteria);
        this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setUri(SEARCH_URI);
        this.exchange.findEntityByCriteria();

    }
}
