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
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.User;
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
	 * @param user
	 * @throws NoSuchMessageException
	 * @throws BusinessException
	 */
	public void checkUniqueAccountUID(final User user) throws NoSuchMessageException, BusinessException {

		final String email = user.getEmail();

		if (StringUtils.isEmpty(email)) return;

		final User criteria = new User();

		criteria.setEmail(email);

		final List<User> results = this.baseDao.findByExample(criteria);

		if (CollectionUtils.isEmpty(results)) return;

		final String messageCode = "account.email.already.used";

		final String message = this.messageSource.getMessage(messageCode, new Object[] { email },
				LocaleContextHolder.getLocale());

		LOGGER.error(message);

		final String defaultMessage = "Email already used";

		throw new BusinessException(messageCode, new Object[] { email }, defaultMessage);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#createAccount(fr.midipascher.domain.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long createAccount(final User user) {

		Preconditions.checkArgument(user != null, "Illegal call to createAccount, user is required");

		user.clearAuthorities();

		final Authority exampleInstance = new Authority();

		exampleInstance.setCode(Authority.RMGR);

		final List<Authority> authorities = this.baseDao.findByExample(exampleInstance);

		Preconditions.checkState(authorities != null, "Illegal state : 'RMGR' authority expected, found none");

		Preconditions.checkState(authorities.size() == 1,
				"Illegal state : one and one only 'RMGR' authority expected, found " + authorities.size());

		user.addAuthority(authorities.get(0));

		checkUniqueAccountUID(user);

		this.baseDao.persist(user);

		return user.getId();

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
	public Long createRestaurant(final Long userId, final Restaurant restaurant) {

		Preconditions.checkArgument(userId != null, "Illegal call to createRestaurant, userId is required");
		Preconditions.checkArgument(restaurant != null, "Illegal call to createRestaurant, restaurant is required");
		Preconditions.checkArgument(restaurant.getId() == null,
				"Illegal call to createRestaurant, restaurant.id should be null");

		final User user = this.baseDao.get(User.class, userId);
		user.addRestaurant(restaurant);
		this.baseDao.persist(user);
		return restaurant.getId();
	}

	/**
	 * @see fr.midipascher.domain.business.Facade#createRestaurant(fr.midipascher.domain.Restaurant)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public Long createRestaurant(final Restaurant restaurant) {

		Preconditions.checkArgument(restaurant != null, "Illegal call to createRestaurant, restaurant is required");

		this.baseDao.persist(restaurant);

		return restaurant.getId();

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#deleteAccount(java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void deleteAccount(final Long userId) {

		Preconditions.checkArgument(userId != null, "Illegal call to deleteAccount, user identifier is required");

		this.baseDao.delete(User.class, userId);

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
	public void deleteRestaurant(final Long userId, final Long restaurantId) {

		Preconditions.checkArgument(restaurantId != null,
				"Illegal call to deleteRestaurant, restaurant identifier is required");

		this.baseDao.get(User.class, userId).removeRestaurant(restaurantId);

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

	/**
	 * @see fr.midipascher.domain.business.Facade#readAccount(java.lang.Long)
	 */
	@Override
	public User readAccount(final Long id) {

		Preconditions.checkArgument(id != null, "Illegal call to readUser, id is required");

		final User user = this.baseDao.get(User.class, id);

		return user;

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readAccount(java.lang.Long,
	 *      boolean)
	 */
	@Override
	@Transactional(readOnly = true)
	public User readAccount(final Long id, final boolean initializeCollections) {

		final User user = readAccount(id);

		if (user != null && initializeCollections) {

			Hibernate.initialize(user.getAuthorities());

			Hibernate.initialize(user.getRestaurants());

		}

		return user;

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readFoodSpecialty(java.lang.Long)
	 */
	@Override
	public FoodSpecialty readFoodSpecialty(final Long foodSpecialtyId) {

		Preconditions.checkArgument(foodSpecialtyId != null,
				"Illegal call to readFoodSpecialty, foodSpecialtyId is required");

		return this.baseDao.get(FoodSpecialty.class, foodSpecialtyId);

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#readRestaurant(java.lang.Long)
	 */
	@Override
	public Restaurant readRestaurant(final Long restaurantId) {

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
	public Restaurant readRestaurant(final Long restaurantId, final boolean initializeCollections) {

		final Restaurant restaurant = readRestaurant(restaurantId);

		if (restaurant != null && initializeCollections) Hibernate.initialize(restaurant.getSpecialties());

		return restaurant;

	}

	/**
	 * @see fr.midipascher.domain.business.Facade#updateAccount(fr.midipascher.domain.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@RolesAllowed({ "ROLE_RMGR", "ROLE_ADMIN" })
	public void updateAccount(final User user) {

		Preconditions.checkArgument(user != null, "Illegal call to updateAccount, user is required");

		Preconditions.checkArgument(user.getId() != null, "Illegal call to updateAccount, user.id is required");

		checkUniqueAccountUID(user);

		final User persistedInstance = this.baseDao.get(User.class, user.getId());

		Preconditions.checkState(persistedInstance != null,
				"Illegal call to updateAccount, provided id should have corresponding user in the store");

		this.baseDao.merge(user);

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

		Preconditions.checkArgument(id != null, "Illegal call to updateFoodSpecialty, foodSpecialty.id is required");

		checkUniqueFoodSpecialtyCode(foodSpecialty);

		final FoodSpecialty persistedInstance = this.baseDao.get(FoodSpecialty.class, id);

		Preconditions.checkState(persistedInstance != null,
				"Illegal call to updateFoodSpecialty, provided id should have corresponding foodSpecialty in store");

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
