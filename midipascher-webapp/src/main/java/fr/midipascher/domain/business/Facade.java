/*
 *
 */
package fr.midipascher.domain.business;

import java.util.List;

import javax.validation.ConstraintViolationException;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.User;
import fr.midipascher.domain.validation.ValidationContext;

/**
 * @author louis.gueye@gmail.com
 */
public interface Facade {

	String	BEAN_ID	= "facade";

	/**
	 * @param foodSpecialty
	 * @return
	 * @throws Throwable
	 * @throws ConstraintViolationException
	 */
	Long createFoodSpecialty(FoodSpecialty foodSpecialty);

	/**
	 * @param restaurant
	 * @return
	 * @throws ConstraintViolationException
	 */
	Long createRestaurant(Restaurant restaurant);

	/**
	 * @param foodSpecialtyId
	 * @throws ConstraintViolationException
	 */
	void deleteFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param restaurantId
	 * @throws ConstraintViolationException
	 */
	void deleteRestaurant(Long restaurantId);

	/**
	 * @param criteria
	 * @return
	 */
	List<Restaurant> findRestaurantsByCriteria(Restaurant criteria);

	/**
	 * @return
	 */
	List<FoodSpecialty> listFoodSpecialties();

	/**
	 * @param foodSpecialtyId
	 * @return
	 */
	FoodSpecialty readFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param restaurantId
	 * @return
	 */
	Restaurant readRestaurant(Long restaurantId);

	/**
	 * @param foodSpecialty
	 * @throws ConstraintViolationException
	 */
	void updateFoodSpecialty(FoodSpecialty foodSpecialty);

	/**
	 * @param restaurant
	 * @throws ConstraintViolationException
	 */
	void updateRestaurant(Restaurant restaurant);

	/**
	 * @param <T>
	 * @param type
	 * @param context
	 * @throws ConstraintViolationException
	 */
	<T extends AbstractEntity> void validate(T type, ValidationContext context);

	/**
	 * @param id
	 * @return
	 */
	User readUser(Long id);

	/**
	 * @param user
	 * @return
	 */
	Long createAccount(User user);

	/**
	 * @param id
	 * @param initializeCollections
	 * @return
	 */
	User readUser(Long id, boolean initializeCollections);

}
