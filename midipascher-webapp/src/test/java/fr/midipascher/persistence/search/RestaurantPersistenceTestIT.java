/*
 *
 */
package fr.midipascher.persistence.search;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.store.BasePersistenceTestIT;
import fr.midipascher.test.TestFixtures;

/**
 * Restaurant database integration testing<br/> CRUD operations are tested<br> Finders are
 * tested<br/>
 *
 * @author louis.gueye@gmail.com
 */
public class RestaurantPersistenceTestIT extends BasePersistenceTestIT {

  /**
   *
   * @param restaurant
   * @param ids
   */
  private void assertRestaurantHasSpecialties(final Restaurant restaurant, final Set<Long> ids) {

    if (restaurant == null && ids == null) {
      return;
    }
    final Set<Long> restaurantIds = new HashSet<Long>();
    for (final FoodSpecialty specialty : restaurant.getSpecialties()) {
      restaurantIds.add(specialty.getId());
    }
    Assert.assertEquals(restaurantIds, ids);
  }

  /**
   *
   * @param result
   * @param ids
   */
  private void assertResultContainsRestaurantIds(final List<Restaurant> result,
                                                 final Set<Long> ids) {
    if (CollectionUtils.isEmpty(result) && ids == null) {
      return;
    }

    final Set<Long> restaurantIds = new HashSet<Long>();
    for (final Restaurant restaurant : result) {
      restaurantIds.add(restaurant.getId());
    }

    Assert.assertTrue(restaurantIds.containsAll(ids));
  }

  /**
   * Given : a valid restaurant<br/> When : one persists the above restaurant<br/> Then : system
   * should retrieve it in database<br/>
   */
  @Test
  public void shouldCreateRestaurant() {
    // Given
    final Restaurant restaurant = validRestaurant();

    // When
    baseDao.persist(restaurant);
    baseDao.flush();

    // Then
    Assert.assertNotNull(restaurant.getId());
    Assert.assertEquals(restaurant, baseDao.get(Restaurant.class, restaurant.getId()));
  }

  /**
   * Given : a valid restaurant<br/> When : one persists the above restaurant and then delete
   * it<br/> Then : system should not retrieve it in database<br/>
   */
  @Test
  public void shouldDeleteRestaurant() {
    // Given
    final Restaurant restaurant = validRestaurant();

    // When
    baseDao.persist(restaurant);
    baseDao.flush();
    baseDao.delete(Restaurant.class, restaurant.getId());
    baseDao.flush();

    // Then
    final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, restaurant.getId());
    Assert.assertNull(persistedRestaurant);
  }

  /**
   * Given : a valid restaurant<br/> When : one updates that restaurant<br/> Then : system should
   * persist changes<br/>
   */
  @Test
  public void shouldUpdateRestaurant() {
    // Given
    final Restaurant restaurant = validRestaurant();

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
    Assert.assertEquals(restaurant.getName(), persistedRestaurant.getName());
  }

  /**
   * Given : a valid restaurant<br/> When : one updates that restaurant and its food
   * specialties<br/> Then : system should persist both changes<br/>
   */
  @Test
  public void shouldUpdateRestaurantFoodSpecialties() {
    // Given
    // Test attached instance state, which corresponds to DB image
    final Restaurant restaurant = baseDao.get(Restaurant.class, 1L);
    Assert.assertNotNull(restaurant);
    Assert.assertNotNull(restaurant.getSpecialties());
    Assert.assertEquals(2, restaurant.getSpecialties().size());
    assertRestaurantHasSpecialties(restaurant, new HashSet<Long>(Arrays.asList(1L, 2L)));

    // When
    // Evict from session
    baseDao.evict(restaurant);
    // Modify detached instance
    final FoodSpecialty sp4 = baseDao.get(FoodSpecialty.class, 4L);
    Assert.assertNotNull(sp4);
    final FoodSpecialty sp5 = baseDao.get(FoodSpecialty.class, 5L);
    Assert.assertNotNull(sp5);
    final FoodSpecialty sp2 = baseDao.get(FoodSpecialty.class, 2L);
    Assert.assertNotNull(sp5);
    final
    Set<FoodSpecialty>
        newSpecialties =
        new HashSet<FoodSpecialty>(Arrays.asList(sp2, sp4, sp5));
    restaurant.setSpecialties(newSpecialties);
    final String newName = RandomStringUtils.random(50);
    restaurant.setName(newName);

    // Re-attach instance
    baseDao.merge(restaurant);
    baseDao.flush();

    // Detached modifications should now be persited
    final Restaurant persistedRestaurant = baseDao.get(Restaurant.class, restaurant.getId());

    // Then
    assertRestaurantHasSpecialties(persistedRestaurant,
                                   new HashSet<Long>(Arrays.asList(2L, 4L, 5L)));
  }

  private Restaurant validRestaurant() {
    final Restaurant restaurant = TestFixtures.validRestaurant();
    restaurant.getSpecialties().clear();

    final FoodSpecialty foodSpecialty = baseDao.get(FoodSpecialty.class, 1L);
    // Should reference at least one valid persisted foodSpecialty
    restaurant.getSpecialties().add(foodSpecialty);
    return restaurant;
  }
}
