/*
 *
 */
package fr.midipascher.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.ResourceUtils;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.test.TestUtils;

/**
 * Restaurant database integration testing<br/>
 * CRUD operations are tested<br>
 * Finders are tested<br/>
 * 
 * @author louis.gueye@gmail.com
 */
public class RestaurantPersistenceTestIT extends BasePersistenceTestIT {

    @Autowired
    private RestaurantDao restaurantDao;

    /**
     * @param longs
     */
    private void assertRestaurantHasSpecialties(final Restaurant restaurant, final Set<Long> ids) {
        if (restaurant == null && ids == null)
            return;
        final Set<Long> restaurantIds = new HashSet<Long>();
        for (final FoodSpecialty specialty : restaurant.getSpecialties()) {
            restaurantIds.add(specialty.getId());
        }
        assertEquals(restaurantIds, ids);
    }

    /**
     * @param longs
     */
    private void assertResultContainsRestaurantIds(final List<Restaurant> result, final Set<Long> ids) {
        if (CollectionUtils.isEmpty(result) && ids == null)
            return;

        final Set<Long> restaurantIds = new HashSet<Long>();
        for (final Restaurant restaurant : result) {
            restaurantIds.add(restaurant.getId());
        }

        assertTrue(restaurantIds.containsAll(ids));
    }

    @Before
    public void onSetUpInTransaction() throws Exception {
        final Connection con = DataSourceUtils.getConnection(dataSource);
        final IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
        final IDataSet dataSet = new FlatXmlDataSetBuilder().build(ResourceUtils
                .getFile("classpath:dbunit/midipascher-test-data.xml"));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
        assertEquals(2, baseDao.findAll(Restaurant.class).size());
    }

    /**
     * Given : a valid restaurant<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should retrieve it in database<br/>
     */
    @Test
    public void shouldCreateRestaurant() {
        // Given
        final Restaurant restaurant = TestUtils.validRestaurant();

        // When
        baseDao.persist(restaurant);
        baseDao.flush();

        // Then
        assertNotNull(restaurant.getId());
        assertEquals(restaurant, baseDao.get(Restaurant.class, restaurant.getId()));
    }

    /**
     * Given : a valid restaurant<br/>
     * When : one persists the above restaurant and then delete it<br/>
     * Then : system should not retrieve it in database<br/>
     */
    @Test
    public void shouldDeleteRestaurant() {
        // Given
        final Restaurant restaurant = TestUtils.validRestaurant();

        // When
        baseDao.persist(restaurant);
        baseDao.flush();
        baseDao.delete(Restaurant.class, restaurant.getId());
        baseDao.flush();

        // Then
        final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, restaurant.getId());
        assertNull(persistedRestaurant);
    }

    /**
     * Given : one restaurant with postal code and city properties valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return restaurant {id = 1} and restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByCityAndPostalCode() {
        // Given
        final Restaurant restaurant = new Restaurant();
        restaurant.setAddress(new Address());
        restaurant.getAddress().setPostalCode("75009");
        restaurant.getAddress().setCity("paris");

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(1L, 2L)));

    }

    /**
     * Given : one food specialty with active property valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return restaurant {id = 1} and restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByFoodSpecialtyActiveProperty() {
        // Given
        final Restaurant restaurant = new Restaurant();
        final FoodSpecialty specialty = new FoodSpecialty();
        final Set<FoodSpecialty> specialties = new HashSet<FoodSpecialty>(Arrays.asList(specialty));
        restaurant.setSpecialties(specialties);

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // Then
        assertEquals(2, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(1L, 2L)));

    }

    /**
     * Given : one food specialty with code valued<br/>
     * When : one search by the above criterion<br/>
     * Then : system should return restaurant {id = 1} and restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByFoodSpecialtyCodeProperty() {
        // Given
        final Restaurant restaurant = new Restaurant();
        final FoodSpecialty specialty = new FoodSpecialty();
        specialty.setCode("sld");
        final Set<FoodSpecialty> specialties = new HashSet<FoodSpecialty>(Arrays.asList(specialty));
        restaurant.setSpecialties(specialties);

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // Then
        assertEquals(2, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(1L, 2L)));

    }

    /**
     * Given : one food specialty with id property valued<br/>
     * When : one search by the above criterion<br/>
     * Then : system should return restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByFoodSpecialtyIdProperty() {
        // Given
        final Restaurant restaurant = new Restaurant();
        final FoodSpecialty specialty = new FoodSpecialty();
        specialty.setId(1L);
        final Set<FoodSpecialty> specialties = new HashSet<FoodSpecialty>(Arrays.asList(specialty));
        restaurant.setSpecialties(specialties);

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // Then
        assertEquals(1, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(1L)));

    }

    /**
     * Given : one food specialty with label property valued<br/>
     * When : one search by the above criterion<br/>
     * Then : system should return restaurant {id = 1}<br/>
     */
    @Test
    public void shouldFindRestaurantByFoodSpecialtyLabelProperty() {
        // Given
        final Restaurant restaurant = new Restaurant();
        final FoodSpecialty specialty = new FoodSpecialty();
        specialty.setLabel("sandwich");
        final Set<FoodSpecialty> specialties = new HashSet<FoodSpecialty>(Arrays.asList(specialty));
        restaurant.setSpecialties(specialties);

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // Then
        assertEquals(1, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(1L)));

    }

    /**
     * Given : one restaurant with halal property valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByHalalCharacteristic() {
        // Given
        final Long persistedRestaurantId = 2L;
        final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, persistedRestaurantId);
        persistedRestaurant.setHalal(true);
        baseDao.persist(persistedRestaurant);
        baseDao.flush();
        final Restaurant restaurant = new Restaurant();
        restaurant.setHalal(true);

        // When
        final List<Restaurant> results = baseDao.findByExample(restaurant);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(2L)));

    }

    /**
     * Given : one restaurant with kosher property valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByKosherCharacteristic() {
        // Given
        final Long persistedRestaurantId = 2L;
        final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, persistedRestaurantId);
        persistedRestaurant.setKosher(true);
        baseDao.persist(persistedRestaurant);
        baseDao.flush();
        final Restaurant restaurant = new Restaurant();
        restaurant.setKosher(true);

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // When
        assertNotNull(results);
        assertEquals(1, results.size());
        assertResultContainsRestaurantIds(results, new HashSet<Long>(Arrays.asList(2L)));

    }

    /**
     * Given : one restaurant with name property valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return restaurant {id = 2}<br/>
     */
    @Test
    public void shouldFindRestaurantByName() {
        // Given
        final Restaurant restaurant = new Restaurant();
        restaurant.setName("juliette");

        // When
        final List<Restaurant> results = restaurantDao.findByExample(restaurant);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(Long.valueOf(1L), results.get(0).getId());

    }

    /**
     * Given : a valid restaurant<br/>
     * When : one updates that restaurant<br/>
     * Then : system should persist changes<br/>
     */
    @Test
    public void shouldUpdateRestaurant() {
        // Given
        final Restaurant restaurant = TestUtils.validRestaurant();
        baseDao.persist(restaurant);
        baseDao.flush();
        baseDao.evict(restaurant);
        final String name = RandomStringUtils.random(50);
        restaurant.setName(name);

        // When
        baseDao.merge(restaurant);
        baseDao.flush();
        final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, restaurant.getId());

        // Then
        assertEquals(restaurant.getName(), persistedRestaurant.getName());
    }

    /**
     * Given : a valid restaurant<br/>
     * When : one updates that restaurant and its food specialties<br/>
     * Then : system should persist both changes<br/>
     */
    @Test
    public void shouldUpdateRestaurantFoodSpecialties() {
        // Given
        // Test attached instance state, which corresponds to DB image
        final Restaurant restaurant = baseDao.get(Restaurant.class, 1L);
        assertNotNull(restaurant);
        assertNotNull(restaurant.getSpecialties());
        assertEquals(2, restaurant.getSpecialties().size());
        assertRestaurantHasSpecialties(restaurant, new HashSet<Long>(Arrays.asList(1L, 2L)));

        // When
        // Evict from session
        baseDao.evict(restaurant);
        // Modify detached instance
        final FoodSpecialty sp4 = baseDao.get(FoodSpecialty.class, 4L);
        assertNotNull(sp4);
        final FoodSpecialty sp5 = baseDao.get(FoodSpecialty.class, 5L);
        assertNotNull(sp5);
        final FoodSpecialty sp2 = baseDao.get(FoodSpecialty.class, 2L);
        assertNotNull(sp5);
        final Set<FoodSpecialty> newSpecialties = new HashSet<FoodSpecialty>(Arrays.asList(sp2, sp4, sp5));
        restaurant.setSpecialties(newSpecialties);
        final String newName = RandomStringUtils.random(50);
        restaurant.setName(newName);

        // Re-attach instance
        baseDao.merge(restaurant);
        baseDao.flush();

        // Detached modifications should now be persited
        final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, restaurant.getId());

        // Then
        assertRestaurantHasSpecialties(persistedRestaurant, new HashSet<Long>(Arrays.asList(2L, 4L, 5L)));
    }
}
