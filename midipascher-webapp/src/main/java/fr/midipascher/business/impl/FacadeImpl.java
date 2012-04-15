/*
 *
 */
package fr.midipascher.business.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.business.Validator;
import fr.midipascher.domain.exceptions.BusinessException;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.persistence.BaseDao;

/**
 * @author louis.gueye@gmail.com
 */
@Service(Facade.BEAN_ID)
public class FacadeImpl implements Facade {

	@Autowired
	private Validator			validator;

	@Autowired
	private BaseDao				baseDao;

	@Autowired
	@Qualifier("messageSources")
	private MessageSource		messageSource;

	private static final Logger	LOGGER	= LoggerFactory.getLogger(FacadeImpl.class);

	/**
	 * @param account
	 * @throws NoSuchMessageException
	 * @throws BusinessException
	 */
	public void checkUniqueAccountUID(final Account account) throws NoSuchMessageException, BusinessException {

		final String email = account.getEmail();

		if (StringUtils.isEmpty(email)) return;

		final Account criteria = new Account();

		criteria.setEmail(email);

		final List<Account> results = this.baseDao.findByExample(criteria);

		if (CollectionUtils.isEmpty(results)) return;

		final String messageCode = "account.email.already.used";

		final String message = this.messageSource.getMessage(messageCode, new Object[] { email },
				LocaleContextHolder.getLocale());

		LOGGER.error(message);

		final String defaultMessage = "Email already used";

		throw new BusinessException(messageCode, new Object[] { email }, defaultMessage);

	}

	/**
	 * @param authority
	 * @throws NoSuchMessageException
	 * @throws BusinessException
	 */
	public void checkUniqueAuthorityCode(final Authority authority) throws NoSuchMessageException, BusinessException {

		final String code = authority.getCode();

		if (StringUtils.isEmpty(code)) return;

		final Authority criteria = new Authority();

		criteria.setCode(code);

		final List<Authority> results = this.baseDao.findByExample(criteria);

		if (CollectionUtils.isEmpty(results)) return;

		final String messageCode = "authority.code.already.used";

		final String message = this.messageSource.getMessage(messageCode, new Object[] { code },
				LocaleContextHolder.getLocale());

		LOGGER.error(message);

		final String defaultMessage = "Code already used";

		throw new BusinessException(messageCode, new Object[] { code }, defaultMessage);

	}

	/**
	 * @param foodSpecialty
	 * @throws NoSuchMessageException
	 * @throws BusinessException
	 */
	public void checkUniqueFoodSpecialtyCode(final FoodSpecialty foodSpecialty) throws NoSuchMessageException,
			BusinessException {

		final String code = foodSpecialty.getCode();

		if (StringUtils.isEmpty(code)) return;

		final FoodSpecialty criteria = new FoodSpecialty();

		criteria.setCode(code);

		final List<FoodSpecialty> results = this.baseDao.findByExample(criteria);

		if (CollectionUtils.isEmpty(results)) return;

		final String messageCode = "foodSpecialty.code.already.used";

		final String message = this.messageSource.getMessage(messageCode, new Object[] { code },
				LocaleContextHolder.getLocale());

		LOGGER.error(message);

		final String defaultMessage = "Code already used";

		throw new BusinessException(messageCode, new Object[] { code }, defaultMessage);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#createAccount(fr.midipascher.domain.Account)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long createAccount(final Account account) {

		Preconditions.checkArgument(account != null, "Illegal call to createAccount, account is required");

		account.clearAuthorities();

		final Authority exampleInstance = new Authority();

		exampleInstance.setCode(Authority.RMGR);

		final List<Authority> authorities = this.baseDao.findByExample(exampleInstance);

		Preconditions.checkState(authorities != null, "Illegal state : 'RMGR' authority expected, found none");

		Preconditions.checkState(authorities.size() == 1,
				"Illegal state : one and one only 'RMGR' authority expected, found " + authorities.size());

		account.addAuthority(authorities.get(0));

		checkUniqueAccountUID(account);

		this.validator.validate(account, ValidationContext.CREATE);

		this.baseDao.persist(account);

		return account.getId();

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#createAuthority(fr.midipascher.domain.Authority)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_ADMIN" })
	public Long createAuthority(final Authority authority) {

		Preconditions.checkArgument(authority != null, "Illegal call to createAuthority, authority is required");

		checkUniqueAuthorityCode(authority);

		this.validator.validate(authority, ValidationContext.CREATE);

		this.baseDao.persist(authority);

		return authority.getId();

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#createFoodSpecialty(fr.midipascher.domain.FoodSpecialty)
	 */
	@Override
	@RolesAllowed("ROLE_ADMIN")
	@Transactional(propagation = Propagation.REQUIRED)
	public Long createFoodSpecialty(final FoodSpecialty foodSpecialty) {

		Preconditions.checkArgument(foodSpecialty != null,
				"Illegal call to createFoodSpecialty, foodSpecialty is required");

		checkUniqueFoodSpecialtyCode(foodSpecialty);

		this.validator.validate(foodSpecialty, ValidationContext.CREATE);

		this.baseDao.persist(foodSpecialty);

		Preconditions.checkState(foodSpecialty.getId() != null, "foodSpecialty id should not be null");

		return foodSpecialty.getId();

	}

	// public void attachPersistentFoodSpecialties(final Restaurant
	// detachedRestaurant) {
	//
	// Set<FoodSpecialty> specialties = detachedRestaurant.getSpecialties();
	//
	// if (CollectionUtils.isEmpty(specialties)) return;
	//
	// Collection<Long> ids = Collections2.transform(specialties, new
	// Function<FoodSpecialty, Long>() {
	//
	// /**
	// * @see com.google.common.base.Function#apply(java.lang.Object)
	// */
	// @Override
	// public Long apply(FoodSpecialty input) {
	// if (input == null) return null;
	// return input.getId();
	// }
	//
	// });
	// LOGGER.warn("{} ids, after transforming input", ids.size());
	//
	// // Remove null ids
	// ids = Collections2.filter(ids, new Predicate<Long>() {
	//
	// /**
	// * @see com.google.common.base.Predicate#apply(java.lang.Object)
	// */
	// @Override
	// public boolean apply(Long input) {
	// return (input != null);
	// }
	//
	// });
	// LOGGER.warn("{} ids, after null filtering", ids.size());
	//
	// detachedRestaurant.clearSpecialties();
	//
	// for ( Long id : ids ) {
	// FoodSpecialty foodSpecialty = readFoodSpecialty(id);
	// if (foodSpecialty != null)
	// detachedRestaurant.addSpecialty(foodSpecialty);
	// }
	//
	// LOGGER.warn("{} specialties, after loading from database",
	// detachedRestaurant.countSpecialties());
	// }

	/**
	 * @see fr.midipascher.domain.business.Facade#createRestaurant(java.lang.Long,
	 *      fr.midipascher.domain.Restaurant)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public Long createRestaurant(final Long accountId, final Restaurant restaurant) {

		Preconditions.checkArgument(accountId != null, "Illegal call to createRestaurant, accountId is required");

		Preconditions.checkArgument(restaurant != null, "Illegal call to createRestaurant, restaurant is required");

		Preconditions.checkArgument(restaurant.getId() == null,
				"Illegal call to createRestaurant, restaurant.id should be null");

		final Account account = this.baseDao.get(Account.class, accountId);

		if (account == null) {
			LOGGER.error("Illegal call to createRestaurant, Account with id {} was not found", accountId);
			throw new BusinessException("account.not.found", new Object[] { accountId },
					"Illegal call to createRestaurant, Account with id " + accountId + " was not found");
		}

		this.validator.validate(restaurant, ValidationContext.CREATE);

		this.baseDao.persist(restaurant);

		account.addRestaurant(restaurant);

		return restaurant.getId();

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#deleteAccount(java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void deleteAccount(final Long accountId) {

		Preconditions.checkArgument(accountId != null, "Illegal call to deleteAccount, account identifier is required");

		this.baseDao.delete(Account.class, accountId);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#deleteFoodSpecialty(java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed("ROLE_ADMIN")
	public void deleteFoodSpecialty(final Long foodSpecialtyId) {

		Preconditions.checkArgument(foodSpecialtyId != null,
				"Illegal call to deleteFoodSpecialty, foodSpecialtyId is required");

		this.baseDao.delete(FoodSpecialty.class, foodSpecialtyId);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#deleteRestaurant(Long,
	 *      java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void deleteRestaurant(final Long accountId, final Long restaurantId) {

		Preconditions.checkArgument(restaurantId != null,
				"Illegal call to deleteRestaurant, restaurant identifier is required");

		this.baseDao.get(Account.class, accountId).removeRestaurant(restaurantId);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#findRestaurantsByCriteria(fr.midipascher.domain.Restaurant)
	 */
	@Override
	public List<Restaurant> findRestaurantsByCriteria(final Restaurant criteria) {

		Preconditions
				.checkArgument(criteria != null, "Illegal call to findRestaurantsByCriteria, criteria is required");

		return this.baseDao.findByExample(criteria);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#inactivateFoodSpecialty(java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_ADMIN" })
	public void inactivateFoodSpecialty(final Long foodSpecialtyId) {

		final FoodSpecialty foodSpecialty = readFoodSpecialty(foodSpecialtyId);

		foodSpecialty.setActive(false);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#listFoodSpecialties()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<FoodSpecialty> listFoodSpecialties() {

		return this.baseDao.findAll(FoodSpecialty.class);

	}

	protected Account readAccount(final Long id) {

		if (id == null) {
			final String message = "Account id was null";
			LOGGER.error(message);
			throw new BusinessException("account.not.found", new Object[] { id }, message);
		}

		final Account account = this.baseDao.get(Account.class, id);

		if (account == null) {
			final String message = "Account was not found for id = " + id;
			LOGGER.error(message);
			throw new BusinessException("account.not.found", new Object[] { id }, message);
		}

		return account;

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readAccount(java.lang.Long,
	 *      boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public Account readAccount(final Long id, final boolean initializeCollections) {

		final Account account = readAccount(id);

		if (account != null && initializeCollections) {

			Hibernate.initialize(account.getAuthorities());

			Hibernate.initialize(account.getRestaurants());

		}

		return account;

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readAuthority(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Authority readAuthority(final Long id) {

		Preconditions.checkArgument(id != null, "Illegal call to readAuthority, id is required");

		return this.baseDao.get(Authority.class, id);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readFoodSpecialty(java.lang.Long)
	 */
	@Override
	public FoodSpecialty readFoodSpecialty(final Long foodSpecialtyId) {

		if (foodSpecialtyId == null) {
			final String message = "FoodSpecialty id was null";
			LOGGER.error(message);
			throw new BusinessException("foodSpecialty.not.found", new Object[] { foodSpecialtyId }, message);
		}

		final FoodSpecialty foodSpecialty = this.baseDao.get(FoodSpecialty.class, foodSpecialtyId);

		if (foodSpecialty == null) {
			final String message = "FoodSpecialty was not found for id = " + foodSpecialtyId;
			LOGGER.error(message);
			throw new BusinessException("foodSpecialty.not.found", new Object[] { foodSpecialtyId }, message);
		}

		return foodSpecialty;

	}

	protected Restaurant readRestaurant(final Long restaurantId) {

		if (restaurantId == null) {
			final String message = "Restaurant id was null";
			LOGGER.error(message);
			throw new BusinessException("restaurant.not.found", new Object[] { restaurantId }, message);
		}

		final Restaurant restaurant = this.baseDao.get(Restaurant.class, restaurantId);

		if (restaurant == null) {
			final String message = "Restaurant was not found for id = " + restaurantId;
			LOGGER.error(message);
			throw new BusinessException("restaurant.not.found", new Object[] { restaurantId }, message);
		}

		return restaurant;
	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readRestaurant(java.lang.Long,
	 *      boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public Restaurant readRestaurant(final Long accountId, final Long restaurantId, final boolean initializeCollections) {

		final Account account = readAccount(accountId);

		if (account.isLocked()) throw new BusinessException("valid.account.required", null, "Account with id '"
				+ accountId + "' was locked");

		final Restaurant restaurant = readRestaurant(restaurantId);

		if (restaurant != null && initializeCollections) Hibernate.initialize(restaurant.getSpecialties());

		return restaurant;

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#updateAccount(fr.midipascher.domain.Account)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void updateAccount(final Long accountId, final Account detached) {

		Preconditions.checkArgument(detached != null, "Illegal call to updateAccount, account is required");

		checkUniqueAccountUID(detached);

		Account persisted = readAccount(accountId);

		persisted.setEmail(detached.getEmail());
		persisted.setFirstName(detached.getFirstName());
		persisted.setLastName(detached.getLastName());
		persisted.setPassword(detached.getPassword());

		this.validator.validate(persisted, ValidationContext.UPDATE);

		// persitedAccount.setRestaurants(account.getRestaurants());

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#updateFoodSpecialty(Long,
	 *      fr.midipascher.domain.FoodSpecialty)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed("ROLE_ADMIN")
	public void updateFoodSpecialty(final Long foodSpecialtyId, final FoodSpecialty detached) {

		Preconditions.checkArgument(detached != null, "Illegal call to updateFoodSpecialty, foodSpecialty is required");

		FoodSpecialty persisted = readFoodSpecialty(foodSpecialtyId);

		checkUniqueFoodSpecialtyCode(detached);

		persisted.setActive(detached.isActive());

		persisted.setCode(detached.getCode());

		persisted.setLabel(detached.getLabel());

		this.validator.validate(persisted, ValidationContext.UPDATE);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#updateRestaurant(fr.midipascher.domain.Restaurant)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void updateRestaurant(Long accountId, final Long restaurantId, final Restaurant detached) {

		Preconditions.checkArgument(detached != null, "Illegal call to updateRestaurant, restaurant is required");

		Account account = readAccount(accountId);

		Set<Restaurant> restaurants = account.getRestaurants();

		Collection<Restaurant> filtered = Collections2.filter(restaurants, new Predicate<Restaurant>() {

			@Override
			public boolean apply(Restaurant input) {
				return input.getId().equals(restaurantId);
			}

		});

		Restaurant persisted = CollectionUtils.sizeIsEmpty(filtered) ? null : filtered.iterator().next();

		if (persisted == null) {
			final String message = "Restaurant was not found for id = " + restaurantId;
			LOGGER.error(message);
			throw new BusinessException("restaurant.not.found", new Object[] { restaurantId }, message);
		}

		persisted.setAddress(detached.getAddress());

		persisted.setCompanyId(detached.getCompanyId());

		persisted.setDescription(detached.getDescription());

		persisted.setHalal(detached.isHalal());

		persisted.setKosher(detached.isKosher());

		persisted.setMainOffer(detached.getMainOffer());

		persisted.setName(detached.getName());

		persisted.setPhoneNumber(detached.getPhoneNumber());

		persisted.setVegetarian(detached.isVegetarian());

		persisted.clearSpecialties();

		for ( FoodSpecialty foodSpecialty : detached.getSpecialties() ) {
			Long foodSpecialtyId = foodSpecialty.getId();
			if (foodSpecialtyId != null) persisted.addSpecialty(readFoodSpecialty(foodSpecialtyId));
		}

		this.validator.validate(persisted, ValidationContext.UPDATE);

	}

}
