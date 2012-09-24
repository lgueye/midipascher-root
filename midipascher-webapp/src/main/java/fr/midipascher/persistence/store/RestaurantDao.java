/*
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.Restaurant;

import java.util.List;

/**
 * @deprecated TODO:delete interface and implementation when done
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
