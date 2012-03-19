/*
 *
 */
package fr.midipascher.persistence;

import java.util.List;

import fr.midipascher.domain.Restaurant;

/**
 * @author louis.gueye@gmail.com
 */
public interface RestaurantDao {

	String	BEAN_ID	= "restaurantDao";

	/**
	 * @param exampleInstance
	 * @return
	 */
	List<Restaurant> findByExample(final Restaurant exampleInstance);

}
