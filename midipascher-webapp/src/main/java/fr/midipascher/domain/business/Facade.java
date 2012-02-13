/*
 *
 */
package fr.midipascher.domain.business;

import java.util.List;

import javax.validation.ConstraintViolationException;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.Authority;
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
	 * @param userId
	 *            TODO
	 * @param restaurantId
	 * @throws ConstraintViolationException
	 */
	void deleteRestaurant(Long userId, Long restaurantId);

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
	User readAccount(Long id);

	/**
	 * @param user
	 * @return
	 */
	Long createAccount(User user);

	/**
	 * @param userId
	 * @param initializeCollections
	 * @return
	 */
	User readAccount(Long userId, boolean initializeCollections);

	/**
	 * @param user
	 */
	void updateAccount(User user);

	/**
	 * @param userId
	 * @param restaurant
	 * @return
	 */
	Long createRestaurant(Long userId, Restaurant restaurant);

	/**
	 * @param restaurantId
	 * @param initializeCollections
	 * @return
	 */
	Restaurant readRestaurant(Long restaurantId, boolean initializeCollections);

	/**
	 * @param foodSpecialtyId
	 */
	void inactivateFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param userId
	 */
	void deleteAccount(Long userId);

	/**
	 * @param authority
	 * @return
	 */
	Long createAuthority(Authority authority);

	/**
	 * @param id
	 * @return
	 */
	Authority readAuthority(Long id);

}
