package fr.midipascher.steps.backend;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.RestaurantSearchFieldsRegistry;
import fr.midipascher.web.WebConstants;
import fr.midipascher.web.resources.FoodSpecialtiesResource;
import fr.midipascher.web.resources.SearchRestaurantsResource;
import org.hamcrest.Matchers;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.OutcomesTable;
import org.jumpmind.symmetric.csv.CsvReader;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author louis.gueye@gmail.com
 */
public class SearchRestaurantSteps extends BackendBaseSteps {

    private static final String SEARCH_URI = UriBuilder.fromPath(WebConstants.BACKEND_PATH)
            .path(SearchRestaurantsResource.class).build().toString();

    private List<URI> createdRestaurantUris = Lists.newArrayList();

    private List<FoodSpecialty> allSpecialties;

    public SearchRestaurantSteps(Exchange exchange) {
        super(exchange);
    }

    @BeforeStory
    public void beforeStory() throws IOException, InterruptedException {
        Exchange exchange = new Exchange();
        exchange.getRequest().setUri(UriBuilder.fromPath(WebConstants.BACKEND_PATH)
                .path(FoodSpecialtiesResource.class).build().toString());
        exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        exchange.readURI();
        allSpecialties = exchange.foodSpcialtiesFromResponse();
        final InputStream searchRestaurantSetupInputStream =
                new ClassPathResource("fr/midipascher/stories/backend/search/search_restaurant_setup.txt")
                        .getInputStream();
        CsvReader csvReader = new CsvReader(searchRestaurantSetupInputStream, '|', Charset.forName("UTF-8"));
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            Restaurant restaurant = fromRow(csvReader);
            restaurant.setPhoneNumber("0101010106");
            final URI uri = CreateRestaurantSteps.createRestaurant(exchange, restaurant);
            Thread.sleep(200);
            createdRestaurantUris.add(uri);

        }
    }

    private Restaurant fromRow(CsvReader csvReader) throws IOException {
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
        Set<FoodSpecialty> specialties = fromCodes(codes);
        restaurant.setSpecialties(specialties);
        return restaurant;
    }

    private Set<FoodSpecialty> fromCodes(Iterable<String> codes) {
        Set<FoodSpecialty> specialties = Sets.newHashSet();
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
    public void searchRestaurantByCriteria(@Named("criterion") String criterion, @Named("value") String value) {
        final Restaurant criteria = new Restaurant();
        setCriteria(criterion, value, criteria);
        this.exchange.getRequest().setBody(criteria);
        this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setUri(SEARCH_URI);
        this.exchange.findEntityByCriteria();

    }

  private void setCriteria(String criterion, String value, final Restaurant criteria) {
    if (RestaurantSearchFieldsRegistry.NAME.equalsIgnoreCase(criterion)) criteria.setName(value);
    else if (RestaurantSearchFieldsRegistry.DESCRIPTION.equalsIgnoreCase(criterion)) criteria
        .setDescription(value);
    else if (RestaurantSearchFieldsRegistry.MAIN_OFFER.equalsIgnoreCase(criterion)) criteria
        .setMainOffer(value);
    else if (RestaurantSearchFieldsRegistry.STREET_ADDRESS.equalsIgnoreCase(criterion))
        criteria.getAddress().setStreetAddress(value);
    else if (RestaurantSearchFieldsRegistry.CITY.equalsIgnoreCase(criterion)) criteria.getAddress().setCity(value);
    else if (RestaurantSearchFieldsRegistry.POSTAL_CODE.equalsIgnoreCase(criterion))
        criteria.getAddress().setPostalCode(value);
    else if (RestaurantSearchFieldsRegistry.COUNTRY_CODE.equalsIgnoreCase(criterion))
        criteria.getAddress().setCountryCode(value);
    else if (RestaurantSearchFieldsRegistry.COMPANY_ID.equalsIgnoreCase(criterion)) criteria
        .setCompanyId(value);
    else if (RestaurantSearchFieldsRegistry.SPECIALTIES.equalsIgnoreCase(criterion)) {
        Iterable<String> codes = Splitter.on(",").trimResults().split(value);
        Set<FoodSpecialty> specialties = fromCodes(codes);
        criteria.setSpecialties(specialties);
    }
  }

  @Then("I should get the following restaurants: $table")
    public void theValuesReturnedAre(ExamplesTable table) {
        List<Restaurant> restaurants = this.exchange.restaurantsFromResponse();
        assertEquals(table.getRowCount(), restaurants.size());
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
        final List<String> sortedCodes = Lists.newArrayList(codes);
        Collections.sort(sortedCodes);
        builder.put(RestaurantSearchFieldsRegistry.SPECIALTIES, Joiner.on(",").join(sortedCodes));
        return builder.build();
    }

    @Given("my current location is \"$location\"")
    public void setCurrentLocation(@Named("location") String location) {
        Restaurant criteria = new Restaurant();
        criteria.getAddress().setFormattedAddress(location);
        this.exchange.getRequest().setBody(criteria);
    }

    @When("I search for restaurants near my location")
    public void searchForRestaurantsNearMyLocation() {
        this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setUri(SEARCH_URI);
        this.exchange.findEntityByCriteria();
    }

    @When("I search for restaurants near my location with additional criteria \"$criteria\"")
    public void searchForRestaurantsNearMyLocationAndCriteria(@Named("criteria") String inlineCriteria) {
        Restaurant criteria = (Restaurant)this.exchange.getRequest().getBody();
        String[] keyValuePairs = inlineCriteria.split(" ");
        for (String keyValuePair : keyValuePairs) {
          final String[] keyValuePairArray = keyValuePair.split("=");
          String key = keyValuePairArray[0];
          String value = keyValuePairArray[1];
          setCriteria(key, value, criteria);
        }

        this.exchange.getRequest().setBody(criteria);
        this.exchange.getRequest().setType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setUri(SEARCH_URI);
        this.exchange.findEntityByCriteria();
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
