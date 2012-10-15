package fr.midipascher.steps.backend;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.hamcrest.Matchers;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.OutcomesTable;
import org.jumpmind.symmetric.csv.CsvReader;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.RestaurantSearchFieldsRegistry;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;
import fr.midipascher.web.resources.SearchRestaurantsResource;

/**
 * @author louis.gueye@gmail.com
 */
public class SearchRestaurantSteps extends BackendBaseSteps {

    private static final String SEARCH_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(SearchRestaurantsResource.class).build().toString();

    private List<URI> createdRestaurantUris = Lists.newArrayList();

    public SearchRestaurantSteps(Exchange exchange) {
        super(exchange);
    }

    @BeforeStory
    public void beforeStory() throws IOException {
        Exchange exchange = new Exchange();
        exchange.getRequest().setUri(UriBuilder.fromPath(WebConstants.BACKEND_PATH)
                                         .path(FoodSpecialtiesResource.class).build().toString());
        exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        exchange.readURI();
        List<FoodSpecialty> allSpecialties = exchange.foodSpcialtiesFromResponse();
        final InputStream searchRestaurantSetupInputStream =
            new ClassPathResource("fr/midipascher/stories/backend/search/search_restaurant_setup.txt")
              .getInputStream();
        CsvReader csvReader = new CsvReader(searchRestaurantSetupInputStream, '|', Charset.forName("UTF-8"));
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
          Restaurant restaurant = fromRow(csvReader, allSpecialties);
          restaurant.setPhoneNumber("0101010106");
          final URI uri = CreateRestaurantSteps.createRestaurant(exchange, restaurant);
          createdRestaurantUris.add(uri);

        }
    }

    private Restaurant fromRow(CsvReader csvReader, List<FoodSpecialty> allSpecialties) throws IOException {
        Restaurant restaurant = new Restaurant();
        final String name = csvReader.get(RestaurantSearchFieldsRegistry.NAME);
        restaurant.setName(name);
        final String description = csvReader.get(RestaurantSearchFieldsRegistry.DESCRIPTION);
        restaurant.setDescription(description);
        final String mainOffer = csvReader.get(RestaurantSearchFieldsRegistry.MAIN_OFFER);
        restaurant.setMainOffer(mainOffer);
        restaurant.getAddress().setStreetAddress(csvReader.get(RestaurantSearchFieldsRegistry.STREET_ADDRESS));
        restaurant.getAddress().setCity(csvReader.get(RestaurantSearchFieldsRegistry.CITY));
        restaurant.getAddress().setPostalCode(csvReader.get(RestaurantSearchFieldsRegistry.POSTAL_CODE));
        restaurant.getAddress().setCountryCode(csvReader.get(RestaurantSearchFieldsRegistry.COUNTRY_CODE));
        restaurant.setCompanyId(csvReader.get(RestaurantSearchFieldsRegistry.COMPANY_ID));
        final String specialtiesCodes = csvReader.get(RestaurantSearchFieldsRegistry.SPECIALTIES);
        Iterable<String> codes = Splitter.on(",").trimResults().split(specialtiesCodes);
        Set<FoodSpecialty> specialties = fromCodes(codes, allSpecialties);
        restaurant.setSpecialties(specialties);
        return restaurant;
    }

    private Set<FoodSpecialty> fromCodes(Iterable<String> codes, final Collection<FoodSpecialty> allSpecialties) {
        Set<FoodSpecialty> specialties =  Sets.newHashSet();
        for (final String code : codes) {
            specialties.addAll(Collections2.filter(allSpecialties, new Predicate<FoodSpecialty>() {
                @Override
                public boolean apply(FoodSpecialty input) {
                    return input != null && code.equalsIgnoreCase(input.getCode());
                }
            }));
        }
        return specialties;
    }

    @When("I search for restaurants which \"$criterion\" matches \"$value\"")
    public void searchRestaurantByName(@Named("criterion") String criterion,  @Named("value") String value) {
        Restaurant criteria = new Restaurant();
        if (RestaurantSearchFieldsRegistry.NAME.equalsIgnoreCase(criterion)) criteria.setName(value);
        else if (RestaurantSearchFieldsRegistry.DESCRIPTION.equalsIgnoreCase(criterion)) criteria.setDescription(value);
        else if (RestaurantSearchFieldsRegistry.MAIN_OFFER.equalsIgnoreCase(criterion)) criteria.setMainOffer(value);
        else if (RestaurantSearchFieldsRegistry.STREET_ADDRESS.equalsIgnoreCase(criterion)) criteria.getAddress().setStreetAddress(value);

        this.exchange.getRequest().setBody(criteria);
        this.exchange.getRequest().setType(MediaType.APPLICATION_JSON);
        this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setUri(SEARCH_URI);
        this.exchange.findEntityByCriteria();

    }

    @Then("I should get the following restaurants: $table")
    public void theValuesReturnedAre(ExamplesTable table) {
        List<Restaurant> restaurants = this.exchange.restaurantsFromResponse();
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
        Collections.sort(Lists.newArrayList(codes));
        builder.put(RestaurantSearchFieldsRegistry.SPECIALTIES, Joiner.on(",").join(codes));
        return builder.build();
    }

    @AfterStory
    public void afterStory() {
      Exchange exchange = new Exchange();
      exchange.setCredentials("louis@rmgr.com", "secret");
      for (URI createdRestaurantUri : createdRestaurantUris) {
        exchange.getRequest().setUri(createdRestaurantUri.toString());
        exchange.deleteEntity();
      }
    }
}
