/*
 *
 */
package fr.midipascher.business.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

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

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
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

		this.baseDao.persist(account);

		return account.getId();

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

		this.baseDao.persist(foodSpecialty);

		Preconditions.checkState(foodSpecialty.getId() != null, "foodSpecialty id should not be null");

		return foodSpecialty.getId();

	}

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
			throw new BusinessException("account.not.found", null, "Illegal call to createRestaurant, Account with id "
					+ accountId + " was not found");
		}

		this.baseDao.persist(restaurant);

		account.addRestaurant(restaurant);

		return restaurant.getId();

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

		Preconditions.checkArgument(id != null, "Illegal call to readUser, id is required");

		final Account account = this.baseDao.get(Account.class, id);

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
	 * @see fr.midipascher.domain.business.Facade#readFoodSpecialty(java.lang.Long)
	 */
	@Override
	public FoodSpecialty readFoodSpecialty(final Long foodSpecialtyId) {

		if (foodSpecialtyId == null) {
			String message = "FoodSpecialty id was null";
			LOGGER.error(message);
			throw new BusinessException("foodSpecialty.not.found", new Object[] { foodSpecialtyId }, message);
		}

		FoodSpecialty foodSpecialty = this.baseDao.get(FoodSpecialty.class, foodSpecialtyId);

		if (foodSpecialty == null) {
			String message = "FoodSpecialty id was not found";
			LOGGER.error(message);
			throw new BusinessException("foodSpecialty.not.found", new Object[] { foodSpecialtyId }, message);
		}

		return foodSpecialty;

	}

	protected Restaurant readRestaurant(final Long restaurantId) {
		Preconditions.checkArgument(restaurantId != null,
				"Illegal call to readRestaurant, restaurant identifier is required");
		return this.baseDao.get(Restaurant.class, restaurantId);
	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readRestaurant(java.lang.Long,
	 *      boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public Restaurant readRestaurant(final Long accountId, final Long restaurantId, final boolean initializeCollections) {

		final Account account = readAccount(accountId);

		if (account == null || account.isLocked()) throw new BusinessException("valid.account.required", null,
				"No account with id '" + accountId + "' was found or account was locked");

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
	public void updateAccount(final Account account) {

		Preconditions.checkArgument(account != null, "Illegal call to updateAccount, account is required");

		Preconditions.checkArgument(account.getId() != null, "Illegal call to updateAccount, account.id is required");

		checkUniqueAccountUID(account);

		final Account persistedInstance = this.baseDao.get(Account.class, account.getId());

		Preconditions.checkState(persistedInstance != null,
				"Illegal call to updateAccount, provided id should have corresponding account in the store");

		this.baseDao.merge(account);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#updateFoodSpecialty(fr.midipascher.domain.FoodSpecialty)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed("ROLE_ADMIN")
	public void updateFoodSpecialty(final FoodSpecialty foodSpecialty) {

		Preconditions.checkArgument(foodSpecialty != null,
				"Illegal call to updateFoodSpecialty, foodSpecialty is required");

		final Long id = foodSpecialty.getId();

		final FoodSpecialty persistedInstance = readFoodSpecialty(id);

		checkUniqueFoodSpecialtyCode(foodSpecialty);

		persistedInstance.setActive(foodSpecialty.isActive());

		persistedInstance.setCode(foodSpecialty.getCode());

		persistedInstance.setLabel(foodSpecialty.getLabel());

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#updateRestaurant(fr.midipascher.domain.Restaurant)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void updateRestaurant(final Restaurant restaurant) {

		Preconditions.checkArgument(restaurant != null, "Illegal call to updateRestaurant, restaurant is required");

		Preconditions.checkArgument(restaurant.getId() != null,
				"Illegal call to updateRestaurant, restaurant.id is required");

		final Restaurant persistedInstance = this.baseDao.get(Restaurant.class, restaurant.getId());

		Preconditions.checkState(persistedInstance != null,
				"Illegal call to updateRestaurant, provided id should have corresponding restaurant in the store");

		persistedInstance.setAddress(restaurant.getAddress());

		persistedInstance.setCompanyId(restaurant.getCompanyId());

		persistedInstance.setDescription(restaurant.getDescription());

		persistedInstance.setHalal(restaurant.isHalal());

		persistedInstance.setKosher(restaurant.isKosher());

		persistedInstance.setMainOffer(restaurant.getMainOffer());

		persistedInstance.setName(restaurant.getName());

		persistedInstance.setPhoneNumber(restaurant.getPhoneNumber());

		persistedInstance.setSpecialties(restaurant.getSpecialties());

		persistedInstance.setVegetarian(restaurant.isVegetarian());

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#validate(fr.midipascher.domain.AbstractEntity,
	 *      fr.midipascher.domain.validation.ValidationContext)
	 */
	@Override
	public <T extends AbstractEntity> void validate(final T type, final ValidationContext context) {

		Preconditions.checkArgument(type != null, "Illegal call to validate, object is required");

		Preconditions.checkArgument(context != null, "Illegal call to validate, validation context is required");

		final Set<ConstraintViolation<T>> constraintViolations = this.validator.validate(type, context.getContext());

		if (CollectionUtils.isEmpty(constraintViolations)) return;

		final Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
				constraintViolations.size());

		final Set<String> classNames = new HashSet<String>();

		for ( final ConstraintViolation<?> violation : constraintViolations ) {

			propagatedViolations.add(violation);

			classNames.add(violation.getLeafBean().getClass().getName());

		}

		throw new ConstraintViolationException(propagatedViolations);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#createAuthority(fr.midipascher.domain.Authority)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_ADMIN" })
	public Long createAuthority(Authority authority) {

		Preconditions.checkArgument(authority != null, "Illegal call to createAuthority, authority is required");

		checkUniqueAuthorityCode(authority);

		this.baseDao.persist(authority);

		return authority.getId();

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readAuthority(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Authority readAuthority(Long id) {

		Preconditions.checkArgument(id != null, "Illegal call to readAuthority, id is required");

		return this.baseDao.get(Authority.class, id);

	}

}
