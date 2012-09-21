/*
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.Restaurant;

import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public interface RestaurantDao {

    String BEAN_ID = "restaurantDao";

    /**
     * @param exampleInstance
     * @return
     */
    List<Restaurant> findByExample(final Restaurant exampleInstance);

}
