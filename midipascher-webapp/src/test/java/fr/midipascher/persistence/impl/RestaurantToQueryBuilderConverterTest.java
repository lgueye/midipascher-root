package fr.midipascher.persistence.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.test.TestFixtures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    String field = "name";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveQueryStringBuilderForDescriptionField() {
    String field = "description";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveQueryStringBuilderForMainOfferField() {
    String field = "mainOffer";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveQueryStringBuilderForStreetAddressField() {
    String field = "address.streetAddress";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveQueryStringBuilderForCityField() {
    String field = "address.city";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveQueryStringBuilderForPostalCodeField() {
    String field = "address.postalCode";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof QueryStringQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveTermQueryBuilderForCompanyIdField() {
    String field = "companyId";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof TermQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveTermQueryBuilderForKosherField() {
    String field = "kosher";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof TermQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveTermQueryBuilderForHalalField() {
    String field = "halal";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof TermQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveTermQueryBuilderForVegetarianField() {
    String field = "vegetarian";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof TermQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveTermQueryBuilderForCountryCodeField() {
    String field = "address.countryCode";
    Object value = "whatever";
    QueryBuilder queryBuilder = underTest.resolveQueryBuilder(field, value);
    assertTrue(queryBuilder instanceof TermQueryBuilder);
  }

  @Test
  public void resolveQueryBuilderShouldResolveTermsQueryBuilderForSpecialtiesField() {
    String field = "specialties";
    Object value = Lists.newArrayList(1L,6L);
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
    assertTrue(criteria.containsKey("name"));
    assertEquals(name, criteria.get("name"));
  }

  @Test
  public void criteriaAsMapShouldAddDescription() {
    Restaurant source = new Restaurant();
    String value = "whatever";
    source.setDescription(value);
    Map<String, Object> criteria = underTest.criteriaAsMap(source);
    assertTrue(criteria.size() == 1);
    String key = "description";
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
    String key = "mainOffer";
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
    String key = "address.streetAddress";
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
    String key = "address.city";
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
    String key = "address.postalCode";
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
    String key = "address.countryCode";
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
    String key = "companyId";
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
    String key = "halal";
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
    String key = "kosher";
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
    String key = "vegetarian";
    assertTrue(criteria.containsKey(key));
    assertEquals(value, criteria.get(key));
  }

  @Test
  public void criteriaAsMapShouldAddSpecialties() {
    Restaurant source = new Restaurant();
    Set<FoodSpecialty> input = Sets.newHashSet(TestFixtures.validFoodSpecialty());
    long id = 5L;
    input.iterator().next().setId(id);
    source.setSpecialties(input);
    Map<String, Object> criteria = underTest.criteriaAsMap(source);
    assertTrue(criteria.size() == 1);
    String key = "specialties";
    assertTrue(criteria.containsKey(key));
    Collection ids = (Collection) criteria.get(key);
    assertTrue( ids.size() == 1);
    assertTrue(ids.contains(id));
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
