/*
 *
 */
package fr.midipascher.domain.business;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public interface Facade {

    String BEAN_ID = "facade";

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
     * @param foodSpecialty
     * @throws ConstraintViolationException
     */
    void updateFoodSpecialty(Long foodSpecialtyId, FoodSpecialty foodSpecialty);

    /**
     * @param restaurantId
     * @param accountId
     * @param restaurant
     * @throws ConstraintViolationException
     */
    void updateRestaurant(Long accountId, Long restaurantId, Restaurant restaurant);

    /**
     * @param authorityId
     */
    void inactivateAuthority(Long authorityId);

    /**
     * @param accountId
     */
    void lockAccount(Long accountId);

}
