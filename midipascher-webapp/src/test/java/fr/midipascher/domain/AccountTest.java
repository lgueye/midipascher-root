/**
 *
 */
package fr.midipascher.domain;

import fr.midipascher.test.TestFixtures;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author louis.gueye@gmail.com
 */
public class AccountTest {

    private Account underTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.underTest = new Account();
    }

    @Test
    public void clearAuthoritiesWillIgnoreIfNullAuthorities() {
        this.underTest.setAuthorities(null);
        this.underTest.clearAuthorities();
        Assert.assertNull(this.underTest.getAuthorities());
    }

    @Test
    public void clearAuthoritiesWillSucceed() {
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(new Authority());
        this.underTest.setAuthorities(authorities);
        this.underTest.clearAuthorities();
        Assert.assertNotNull(this.underTest.getAuthorities());
        Assert.assertTrue(CollectionUtils.sizeIsEmpty(this.underTest.getAuthorities()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAuthorityWillThrowIllegalArgumentExceptionWithNullInput() {
        // Given
        Authority authority = null;

        // When
        this.underTest.addAuthority(authority);
    }

    @Test
    public void addAuthorityWontFailWithNullSet() {
        // Given
        Authority authority = new Authority();

        int countAuthorities = this.underTest.countAuthorities();
        // When
        this.underTest.addAuthority(authority);

        Assert.assertEquals(countAuthorities + 1, this.underTest.countAuthorities());

    }

    @Test
    public void countAuthoritiesShouldReturnZeroWithNullSet() {
        Assert.assertEquals(0, this.underTest.countAuthorities());
    }

    @Test
    public void countAuthoritiesShouldReturnSetSize() {
        Set<Authority> authorities = new HashSet<Authority>();
        this.underTest.setAuthorities(authorities);
        Assert.assertEquals(0, this.underTest.countAuthorities());
        this.underTest.addAuthority(new Authority());
        Assert.assertEquals(1, this.underTest.countAuthorities());
    }

    @Test
    public void countAuthoritiesShouldReturnZeroWithEmptySet() {
        this.underTest.setAuthorities(new HashSet<Authority>());
        Assert.assertEquals(0, this.underTest.countAuthorities());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRestaurantWillThrowIllegalArgumentExceptionWithNullInput() {
        // Given
        Restaurant restaurant = null;

        // When
        this.underTest.addRestaurant(restaurant);
    }

    @Test
    public void addRestaurantWontFailWithNullSet() {
        // Given
        Restaurant restaurant = new Restaurant();

        int countRestaurants = this.underTest.countRestaurants();
        // When
        this.underTest.addRestaurant(restaurant);

        Assert.assertEquals(countRestaurants + 1, this.underTest.countRestaurants());

    }

    @Test
    public void countRestaurantsShouldReturnZeroWithNullSet() {
        Assert.assertEquals(0, this.underTest.countRestaurants());
    }

    @Test
    public void countRestaurantsShouldReturnSetSize() {
        Set<Restaurant> restaurants = new HashSet<Restaurant>();
        this.underTest.setRestaurants(restaurants);
        Assert.assertEquals(0, this.underTest.countRestaurants());
        this.underTest.addRestaurant(new Restaurant());
        Assert.assertEquals(1, this.underTest.countRestaurants());
    }

    @Test
    public void countRestaurantsShouldReturnZeroWithEmptySet() {
        this.underTest.setRestaurants(new HashSet<Restaurant>());
        Assert.assertEquals(0, this.underTest.countRestaurants());
    }

    @Test
    public void removeRestaurantWontFailWithNullRestaurantsCollection() {
        this.underTest.setRestaurants(null);
        try {
            this.underTest.removeRestaurant(5L);
        } catch (Throwable th) {
            fail("No exception expected");
        }
    }

    @Test
    public void removeRestaurantWontFailWithEmptyRestaurantsCollection() {
        this.underTest.setRestaurants(new HashSet<Restaurant>());
        try {
            this.underTest.removeRestaurant(5L);
        } catch (Throwable th) {
            fail("No exception expected");
        }
    }

    @Test
    public void removeRestaurantWontFailWithNullProvidedRestaurantId() {
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setId(9L);
        this.underTest.addRestaurant(restaurant);

        try {
            this.underTest.removeRestaurant(null);
        } catch (Throwable th) {
            fail("No exception expected");
        }
    }

    @Test
    public void removeRestaurantWontFailWithNullRestaurantIdInCollection() {
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setId(null);
        this.underTest.addRestaurant(restaurant);

        try {
            this.underTest.removeRestaurant(null);
        } catch (Throwable th) {
            fail("No exception expected");
        }
    }

    @Test
    public void removeRestaurantShouldSucceed() {
        Long id = 4L;
        int countRestaurant;
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.setId(id);
        this.underTest.addRestaurant(restaurant);
        countRestaurant = this.underTest.countRestaurants();
        this.underTest.removeRestaurant(id);
        assertEquals(countRestaurant - 1, this.underTest.countRestaurants());
    }
}
