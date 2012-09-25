/**
 *
 */
package fr.midipascher.persistence.search;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import fr.midipascher.TestConstants;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.JsonByteArrayToRestaurantConverter;
import fr.midipascher.persistence.search.RestaurantToJsonByteArrayConverter;
import fr.midipascher.persistence.search.SearchIndices;
import fr.midipascher.persistence.search.SearchTypes;
import fr.midipascher.test.TestFixtures;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.junit.Assert.*;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
@ContextConfiguration( {TestConstants.SERVER_CONTEXT, TestConstants.SEARCH_CONTEXT_TEST})
public class SearchRestaurantTestIT {

    @Autowired
    @Qualifier(RestaurantToJsonByteArrayConverter.BEAN_ID)
    private RestaurantToJsonByteArrayConverter restaurantToJsonByteArrayConverter;

    @Autowired
    @Qualifier(JsonByteArrayToRestaurantConverter.BEAN_ID)
    private JsonByteArrayToRestaurantConverter jsonByteArrayToRestaurantConverter;

    @Value("classpath:/elasticsearch/midipascher/_settings.json")
    private Resource indexSettings;

    @Value("classpath:/elasticsearch/midipascher/restaurant.json")
    private Resource restaurantsMapping;

    @Autowired
    private Client underTest;

    private static final String INDEX_NAME = SearchIndices.midipascher.toString();
    private static final String TYPE_NAME = SearchTypes.restaurant.toString();

    @Before
    public void configureSearchEngine() throws Exception {

        // Deletes index if already exists
        if (this.underTest.admin().indices().prepareExists(INDEX_NAME).execute().actionGet().exists()) {
            DeleteIndexResponse deleteIndexResponse = this.underTest.admin().indices().prepareDelete(INDEX_NAME)
                    .execute().actionGet();
            deleteIndexResponse.acknowledged();
        }

        String indexSettingsAsString = Resources.toString(this.indexSettings.getURL(), Charsets.UTF_8);
        CreateIndexResponse createIndexResponse = this.underTest.admin().indices().prepareCreate(INDEX_NAME)
                .setSettings(indexSettingsAsString).execute().actionGet();
        createIndexResponse.acknowledged();

        String restaurantMappingAsString = Resources.toString(this.restaurantsMapping.getURL(), Charsets.UTF_8);
        PutMappingResponse putMappingResponse = this.underTest.admin().indices().preparePutMapping(INDEX_NAME)
                .setType(TYPE_NAME).setSource(restaurantMappingAsString).execute().actionGet();
        putMappingResponse.acknowledged();

        //this.underTest.admin().cluster().prepareHealth(INDEX_NAME).setWaitForYellowStatus().execute().actionGet();
    }

    @Test
    public void findByDescriptionShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String description;
        String query;

        // Given I index that data
        description = "Gouts et saveurs";
        restaurant = TestFixtures.validRestaurant();
        restaurant.setDescription(description);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());
        expectedHitsCount = 1;

        // When I search
        query = "gouts";
        actualResponse = findByDescription(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(description, restaurant.getDescription());

        // When I search
        query = "goûts";
        actualResponse = findByDescription(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(description, restaurant.getDescription());

        // When I search
        query = "saveurs";
        actualResponse = findByDescription(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(description, restaurant.getDescription());
    }

    @Test
    public void findByCompanyIdShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String companyId;
        String query;

        // Given I index that data
        companyId = "41939503300014";
        restaurant = TestFixtures.validRestaurant();
        restaurant.setCompanyId(companyId);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());

        // When I search
        query = "41939503300014";
        actualResponse = findByCompanyId(query);
        // Then I should get 1 hit
        expectedHitsCount = 1;
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(companyId, restaurant.getCompanyId());

        // When I search
        query = "4193950330001";
        actualResponse = findByCompanyId(query);
        // Then I should get 0 hit
        expectedHitsCount = 0;
        assertHitsCount(expectedHitsCount, actualResponse);
    }

    @Test
    public void findByNameShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String name;
        String query;

        // Given I index that data
        name = "Gouts et saveurs";
        restaurant = TestFixtures.validRestaurant();
        restaurant.setName(name);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());
        expectedHitsCount = 1;

        // When I search
        query = "gouts";
        actualResponse = findByName(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(name, restaurant.getName());

        // When I search
        query = "goûts";
        actualResponse = findByName(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(name, restaurant.getName());

        // When I search
        query = "saveurs";
        actualResponse = findByName(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(name, restaurant.getName());
    }

    @Test
    public void findByMainOfferShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String mainOffer;
        String query;

        // Given I index that data
        mainOffer = "Gouts et saveurs";
        restaurant = TestFixtures.validRestaurant();
        restaurant.setMainOffer(mainOffer);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());
        expectedHitsCount = 1;

        // When I search
        query = "gouts";
        actualResponse = findByMainOffer(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(mainOffer, restaurant.getMainOffer());

        // When I search
        query = "goûts";
        actualResponse = findByMainOffer(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(mainOffer, restaurant.getMainOffer());

        // When I search
        query = "saveurs";
        actualResponse = findByMainOffer(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(mainOffer, restaurant.getMainOffer());
    }

    @Test
    public void findByStreetAddressShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String streetAddress;
        String query;

        // Given I index that data
        streetAddress = "Rue Jean Jaurès";
        restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setStreetAddress(streetAddress);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());
        expectedHitsCount = 1;

        // When I search
        query = "jaures";
        actualResponse = findByStreetAddress(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(streetAddress, restaurant.getAddress().getStreetAddress());

        // When I search
        query = "jaurès";
        actualResponse = findByStreetAddress(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(streetAddress, restaurant.getAddress().getStreetAddress());

        // When I search
        query = "jean";
        actualResponse = findByStreetAddress(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(streetAddress, restaurant.getAddress().getStreetAddress());
    }

    @Test
    public void findByCityShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String city;
        String query;

        // Given I index that data
        city = "New-York";
        restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setCity(city);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());
        expectedHitsCount = 1;

        // When I search
        query = "york";
        actualResponse = findByCity(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(city, restaurant.getAddress().getCity());

        // When I search
        query = "new";
        actualResponse = findByCity(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(city, restaurant.getAddress().getCity());

    }

    @Test
    public void findByPostalCodeShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String postalCode;
        String query;

        // Given I index that data
        postalCode = "Paris 75009";
        restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setPostalCode(postalCode);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());
        expectedHitsCount = 1;

        // When I search
        query = "75009";
        actualResponse = findByPostalCode(query);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(postalCode, restaurant.getAddress().getPostalCode());

    }

    @Test
    public void findByCountryCodeShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        String countryCode;
        String query;

        // Given I index that data
        countryCode = "GB";
        restaurant = TestFixtures.validRestaurant();
        restaurant.getAddress().setCountryCode(countryCode);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());

        // When I search
        query = "GB";
        actualResponse = findByCountryCode(query);
        // Then I should get 1 hit
        expectedHitsCount = 1;
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(countryCode, restaurant.getAddress().getCountryCode());

        // When I search
        query = "gb";
        actualResponse = findByCountryCode(query);
        // Then I should get 0 hit
        expectedHitsCount = 0;
        assertHitsCount(expectedHitsCount, actualResponse);
    }

    @Test
    public void findByKosherPropertyShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        boolean kosher;
        boolean query;

        // Given I index that data
        kosher = true;
        restaurant = TestFixtures.validRestaurant();
        restaurant.setKosher(kosher);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());

        // When I search
        query = true;
        actualResponse = findByKosherProperty(query);
        // Then I should get 1 hit
        expectedHitsCount = 1;
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(kosher, restaurant.isKosher());

    }

    @Test
    public void findByHalalPropertyShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        boolean halal;
        boolean query;

        // Given I index that data
        halal = true;
        restaurant = TestFixtures.validRestaurant();
        restaurant.setHalal(halal);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());

        // When I search
        query = true;
        actualResponse = findByHalalProperty(query);
        // Then I should get 1 hit
        expectedHitsCount = 1;
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(halal, restaurant.isHalal());

    }

    @Test
    public void findByVegetarianPropertyShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Restaurant restaurant;
        boolean vegetarian;
        boolean query;

        // Given I index that data
        vegetarian = true;
        restaurant = TestFixtures.validRestaurant();
        restaurant.setVegetarian(vegetarian);
        indexRestaurant(id, restaurant);
        indexRestaurant(1L, TestFixtures.validRestaurant());

        // When I search
        query = true;
        actualResponse = findByVegetarianProperty(query);
        // Then I should get 1 hit
        expectedHitsCount = 1;
        assertHitsCount(expectedHitsCount, actualResponse);
        restaurant = extractRestaurantFromResponse(actualResponse);
        assertEquals(vegetarian, restaurant.isVegetarian());

    }

    private SearchResponse findByName(final String query) {
        QueryStringQueryBuilder queryString = QueryBuilders.queryString(query).field("name");
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByDescription(final String query) {
        QueryStringQueryBuilder queryString = QueryBuilders.queryString(query).field("description");
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByMainOffer(final String query) {
        QueryStringQueryBuilder queryString = QueryBuilders.queryString(query).field("mainOffer");
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByStreetAddress(final String query) {
        QueryStringQueryBuilder queryString = QueryBuilders.queryString(query).field("address.streetAddress");
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByPostalCode(final String query) {
        QueryStringQueryBuilder queryString = QueryBuilders.queryString(query).field("address.postalCode");
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByCity(final String query) {
        QueryStringQueryBuilder queryString = QueryBuilders.queryString(query).field("address.city");
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByCompanyId(final String query) {
        QueryBuilder queryString = QueryBuilders.termQuery("companyId", query);
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByCountryCode(final String query) {
        QueryBuilder queryString = QueryBuilders.termQuery("address.countryCode", query);
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByKosherProperty(final boolean query) {
        QueryBuilder queryString = QueryBuilders.termQuery("kosher", query);
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByHalalProperty(final boolean query) {
        QueryBuilder queryString = QueryBuilders.termQuery("halal", query);
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    private SearchResponse findByVegetarianProperty(final boolean query) {
        QueryBuilder queryString = QueryBuilders.termQuery("vegetarian", query);
        return this.underTest.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryString).execute().actionGet();
    }

    /**
     * @param id
     * @param restaurant
     */
    private void indexRestaurant(final Long id, final Restaurant restaurant) {
        this.underTest.prepareIndex(INDEX_NAME, TYPE_NAME)//
                .setRefresh(true) //
                .setSource(this.restaurantToJsonByteArrayConverter.convert(restaurant)) //
                .execute().actionGet();
    }

    private void assertHitsCount(final int expectedHitsCount, final SearchResponse actualResponse) {

        assertNotNull(actualResponse);

        final SearchHits hits = actualResponse.getHits();

        assertNotNull(hits);

        final int totalHits = (int) hits.getTotalHits();
//        System.out.println("totalHits = " + totalHits);
//        System.out.println("expectedHitsCount = " + expectedHitsCount);
        assertTrue(expectedHitsCount == totalHits);

    }

    /**
     * @param actualResponse
     * @return
     */
    private Restaurant extractRestaurantFromResponse(final SearchResponse actualResponse) {

        assertNotNull(actualResponse);

        assertNotNull(actualResponse.getHits());

        final SearchHits hits = actualResponse.getHits();

        assertEquals(1, hits.getTotalHits());

        final SearchHit hit = hits.getHits()[0];

        assertNotNull(hit);

        assertNotNull(hit.source());

        final Restaurant advert = this.jsonByteArrayToRestaurantConverter.convert(hit.source());

        return advert;

    }

}
