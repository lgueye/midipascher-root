/*
 *
 */
package fr.midipascher.domain.business;

import java.util.List;

import javax.validation.ConstraintViolationException;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;

/**
 * @author louis.gueye@gmail.com
 */
public interface Facade {

	String	BEAN_ID	= "facade";

	/**
	 * @param account
	 * @return
	 */
	Long createAccount(Account account);

	/**
	 * @param authority
	 * @return
	 */
	Long createAuthority(Authority authority);

	/**
	 * @param restaurant
	 * @return
	 * @throws ConstraintViolationException
	 *             Long createRestaurant(Restaurant restaurant);
	 */

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
	 * @param accountId
	 */
	void deleteAccount(Long accountId);

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
	 * @param restaurantId
	 * @return Restaurant readRestaurant(Long restaurantId);
	 */

	/**
	 * @param criteria
	 * @return
	 */
	List<Restaurant> findRestaurantsByCriteria(Restaurant criteria);

	/**
	 * @param foodSpecialtyId
	 */
	void inactivateFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param id
	 * @return Account readAccount(Long id);
	 */

	/**
	 * @return
	 */
	List<FoodSpecialty> listFoodSpecialties();

	/**
	 * @param accountId
	 * @param initializeCollections
	 * @return
	 */
	Account readAccount(Long accountId, boolean initializeCollections);

	/**
	 * @param id
	 * @return
	 */
	Authority readAuthority(Long id);

	/**
	 * @param foodSpecialtyId
	 * @return
	 */
	FoodSpecialty readFoodSpecialty(Long foodSpecialtyId);

	/**
	 * @param accountId
	 * @param restaurantId
	 * @param initializeCollections
	 * @return
	 */
	Restaurant readRestaurant(Long accountId, Long restaurantId, boolean initializeCollections);

	/**
	 * @param account
	 */
	void updateAccount(Long accountId, Account account);

	/**
	 * @param foodSpecialtyId
	 *            TODO
	 * @param foodSpecialty
	 * @throws ConstraintViolationException
	 */
	void updateFoodSpecialty(Long foodSpecialtyId, FoodSpecialty foodSpecialty);

	/**
	 * @param restaurant
	 * @throws ConstraintViolationException
	 */
	void updateRestaurant(Restaurant restaurant);

}
