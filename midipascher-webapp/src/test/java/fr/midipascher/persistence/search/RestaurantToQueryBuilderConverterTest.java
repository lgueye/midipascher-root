package fr.midipascher.persistence.search;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.RestaurantSearchFieldsRegistry;
import fr.midipascher.persistence.search.RestaurantToQueryBuilderConverter;
import fr.midipascher.test.TestFixtures;
import org.elasticsearch.index.query.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * User: lgueye Date: 17/09/12 Time: 15:04
 */
public class RestaurantToQueryBuilderConverterTest {

    private RestaurantToQueryBuilderConverter underTest;

    @Before
    public void before() {
        underTest = new RestaurantToQueryBuilderConverter();
    }

    @Test
    public void noCriteriaShouldReturnTrueWithNullInput() {
        Map<String, Object> criteria = null;
        boolean noCriteria = underTest.noCriteria(criteria);
        assertTrue(noCriteria);
    }

    @Test
    public void noCriteriaShouldReturnTrueWithEmptyInput() {
        Map<String, Object> criteria = Maps.newHashMap();
        boolean noCriteria = underTest.noCriteria(criteria);
        assertTrue(noCriteria);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void resolveQueryBuilderShouldThrowUnsupportedOperationExceptionWithNullField() {
        String field = null;
        String value = "wathever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void resolveQueryBuilderShouldThrowUnsupportedOperationExceptionWithEmptyField() {
        String field = "";
        String value = "wathever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void resolveQueryBuilderShouldThrowUnsupportedOperationExceptionWithUnknownField() {
        String field = "whatever";
        String value = "wathever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);

    }

    @Test
    public void resolveQueryBuilderShouldResolveQueryStringBuilderForNameField() {
        String field = RestaurantSearchFieldsRegistry.NAME;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveQueryStringBuilderForDescriptionField() {
        String field = RestaurantSearchFieldsRegistry.DESCRIPTION;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveQueryStringBuilderForMainOfferField() {
        String field = RestaurantSearchFieldsRegistry.MAIN_OFFER;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveQueryStringBuilderForStreetAddressField() {
        String field = RestaurantSearchFieldsRegistry.STREET_ADDRESS;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveQueryStringBuilderForCityField() {
        String field = RestaurantSearchFieldsRegistry.CITY;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveQueryStringBuilderForPostalCodeField() {
        String field = RestaurantSearchFieldsRegistry.POSTAL_CODE;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveTermQueryBuilderForCompanyIdField() {
        String field = RestaurantSearchFieldsRegistry.COMPANY_ID;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof TermQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveTermQueryBuilderForKosherField() {
        String field = RestaurantSearchFieldsRegistry.KOSHER;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof TermQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveTermQueryBuilderForHalalField() {
        String field = RestaurantSearchFieldsRegistry.HALAL;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof TermQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveTermQueryBuilderForVegetarianField() {
        String field = RestaurantSearchFieldsRegistry.VEGETARIAN;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof TermQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveTermQueryBuilderForCountryCodeField() {
        String field = RestaurantSearchFieldsRegistry.COUNTRY_CODE;
        Object value = "whatever";
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof TermQueryBuilder);
    }

    @Test
    public void resolveQueryBuilderShouldResolveTermsQueryBuilderForSpecialtiesField() {
        String field = RestaurantSearchFieldsRegistry.SPECIALTIES;
        Object value = Lists.newArrayList("SDW", "CHN");
        QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
        assertTrue(queryBuilder instanceof TermsQueryBuilder);
    }

    @Test
    public void criteriaAsMapShouldReturnNullWithNullInput() {
        Restaurant source = null;
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertNull(criteria);
    }

    @Test
    public void criteriaAsMapShouldReturnEmptyMap() {
        Restaurant source = new Restaurant();
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 0);
    }

    @Test
    public void criteriaAsMapShouldAddName() {
        Restaurant source = new Restaurant();
        String name = "whatever";
        source.setName(name);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.NAME;
        assertTrue(criteria.containsKey(key));
        assertEquals(name, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddDescription() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setDescription(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.DESCRIPTION;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddMainOffer() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setMainOffer(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.MAIN_OFFER;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddStreetAddress() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setAddress(new Address());
        source.getAddress().setStreetAddress(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.STREET_ADDRESS;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddStreetCity() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setAddress(new Address());
        source.getAddress().setCity(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.CITY;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddPostalCode() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setAddress(new Address());
        source.getAddress().setPostalCode(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.POSTAL_CODE;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddCountryCode() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setAddress(new Address());
        source.getAddress().setCountryCode(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.COUNTRY_CODE;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddCompanyId() {
        Restaurant source = new Restaurant();
        String value = "whatever";
        source.setCompanyId(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.COMPANY_ID;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddHalal() {
        Restaurant source = new Restaurant();
        Boolean value = false;
        source.setHalal(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.HALAL;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddKosher() {
        Restaurant source = new Restaurant();
        Boolean value = true;
        source.setKosher(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.KOSHER;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddVegetarian() {
        Restaurant source = new Restaurant();
        Boolean value = true;
        source.setVegetarian(value);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.VEGETARIAN;
        assertTrue(criteria.containsKey(key));
        assertEquals(value, criteria.get(key));
    }

    @Test
    public void criteriaAsMapShouldAddSpecialties() {
        Restaurant source = new Restaurant();
      final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
      Set<FoodSpecialty> input = Sets.newHashSet(foodSpecialty);
        source.setSpecialties(input);
        Map<String, Object> criteria = underTest.criteriaAsMap(source);
        assertTrue(criteria.size() == 1);
        String key = RestaurantSearchFieldsRegistry.SPECIALTIES;
        assertTrue(criteria.containsKey(key));
        Collection<String> codes = (Collection<String>) criteria.get(key);
        assertTrue(codes.size() == 1);
        assertTrue(codes.contains(foodSpecialty.getCode()));
    }

    @Test
    public void convertShouldResolveToMatchAllQueryBuilder() {
        Restaurant source;
        QueryBuilder queryBuilder;

        source = null;
        queryBuilder = underTest.convert(source);
        assertTrue(queryBuilder instanceof MatchAllQueryBuilder);

        source = new Restaurant();
        queryBuilder = underTest.convert(source);
        assertTrue(queryBuilder instanceof MatchAllQueryBuilder);
    }

    @Test
    public void convertShouldResolveToBoolQueryBuilder() {
        Restaurant source;
        QueryBuilder queryBuilder;

        source = new Restaurant();
        source.setCompanyId("whatever");
        queryBuilder = underTest.convert(source);
        assertTrue(queryBuilder instanceof BoolQueryBuilder);
        assertTrue(((BoolQueryBuilder) queryBuilder).hasClauses());
    }

}
