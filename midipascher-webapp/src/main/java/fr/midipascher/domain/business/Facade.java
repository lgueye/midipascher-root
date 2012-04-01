/*
 *
 */
package fr.midipascher.domain.business;

import java.util.List;

import javax.validation.ConstraintViolationException;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
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
	 * @param accountId
	 * @param restaurant
	 * @return
	 */
	Long createRestaurant(final Long accountId, final Restaurant restaurant);

	/**
	 * @param restaurant
	 * @return
	 * @throws ConstraintViolationException
	 *             Long createRestaurant(Restaurant restaurant);
	 */

	/**
	 * @param foodSpecialtyId
	 * @throws ConstraintViolationException
	 */
	void deleteFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param accountId
	 *            TODO
	 * @param restaurantId
	 * @throws ConstraintViolationException
	 */
	void deleteRestaurant(Long accountId, Long restaurantId);

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
	 * @return Restaurant readRestaurant(Long restaurantId);
	 */

	/**
	 * @param foodSpecialtyId TODO
	 * @param foodSpecialty
	 * @throws ConstraintViolationException
	 */
	void updateFoodSpecialty(Long foodSpecialtyId, FoodSpecialty foodSpecialty);

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
	 * @return Account readAccount(Long id);
	 */

	/**
	 * @param account
	 * @return
	 */
	Long createAccount(Account account);

	/**
	 * @param accountId
	 * @param initializeCollections
	 * @return
	 */
	Account readAccount(Long accountId, boolean initializeCollections);

	/**
	 * @param account
	 */
	void updateAccount(Account account);

	/**
	 * @param accountId
	 * @param restaurantId
	 * @param initializeCollections
	 * @return
	 */
	Restaurant readRestaurant(Long accountId, Long restaurantId, boolean initializeCollections);

	/**
	 * @param foodSpecialtyId
	 */
	void inactivateFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param accountId
	 */
	void deleteAccount(Long accountId);

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
