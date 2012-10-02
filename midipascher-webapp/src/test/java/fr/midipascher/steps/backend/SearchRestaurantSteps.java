package fr.midipascher.steps.backend;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.RestaurantSearchFieldsRegistry;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.SearchRestaurantsResource;
import org.hamcrest.Matchers;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.OutcomesTable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    @Then("I should get the following restaurants: $table")
    public void theValuesReturnedAre(ExamplesTable table) {
        List<Restaurant> restaurants = this.exchange.fromResponse();
        for (int i = 0; i < table.getRowCount(); i++) {
            Map<String, String> actualRow = actualRow(restaurants.get(i)); // obtained from another step invocation
            OutcomesTable outcomes = new OutcomesTable();
            Map<String, String> expectedRow = table.getRow(i);
            for (String key : expectedRow.keySet()) {
                outcomes.addOutcome(key, actualRow.get(key), Matchers.equalTo(expectedRow.get(key)));
            }
            outcomes.verify();
        }

    }

    private Map<String, String> actualRow(Restaurant restaurant) {
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
        builder.put(RestaurantSearchFieldsRegistry.NAME, restaurant.getName());
        builder.put(RestaurantSearchFieldsRegistry.DESCRIPTION, restaurant.getDescription());
        builder.put(RestaurantSearchFieldsRegistry.MAIN_OFFER, restaurant.getMainOffer());
        builder.put(RestaurantSearchFieldsRegistry.STREET_ADDRESS, restaurant.getAddress().getStreetAddress());
        builder.put(RestaurantSearchFieldsRegistry.CITY, restaurant.getAddress().getCity());
        builder.put(RestaurantSearchFieldsRegistry.POSTAL_CODE, restaurant.getAddress().getPostalCode());
        builder.put(RestaurantSearchFieldsRegistry.COUNTRY_CODE, restaurant.getAddress().getCountryCode());
        builder.put(RestaurantSearchFieldsRegistry.COMPANY_ID, restaurant.getCompanyId());
        Collection codes = Collections2.transform(restaurant.getSpecialties(), new Function<FoodSpecialty, Object>() {
            @Override
            public String apply(FoodSpecialty input) {
                return input.getCode();
            }
        });
        builder.put(RestaurantSearchFieldsRegistry.SPECIALTIES, Joiner.on(",").join(codes));
        return builder.build();
    }
}
