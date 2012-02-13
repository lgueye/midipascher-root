/**
 * 
 */
package fr.midipascher.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import fr.midipascher.business.impl.FacadeImpl;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Persistable;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.User;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.exceptions.BusinessException;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.persistence.BaseDao;
import fr.midipascher.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class FacadeImplTest {

	@Mock
	private Validator		validator;

	@Mock
	private BaseDao			baseDao;

	@Mock
	private MessageSource	messageSource;

	@InjectMocks
	private final Facade	underTest	= new FacadeImpl();

	@Test
	public void createFoodSpecialtyShouldInvokePersistence() throws Throwable {

		// Variables
		FoodSpecialty foodSpecialty;
		String code;
		List<FoodSpecialty> results;

		// Given
		code = "BLABLA";
		results = null;
		LocaleContextHolder.setLocale(Locale.FRENCH);
		foodSpecialty = Mockito.mock(FoodSpecialty.class);

		// When
		Mockito.when(foodSpecialty.getId()).thenReturn(2L);
		Mockito.when(foodSpecialty.getCode()).thenReturn(code);
		Mockito.when(this.baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
		this.underTest.createFoodSpecialty(foodSpecialty);

		// Then
		Mockito.verify(foodSpecialty, Mockito.times(2)).getId();
		Mockito.verify(foodSpecialty).getCode();
		Mockito.verify(this.baseDao).findByExample(Matchers.any(FoodSpecialty.class));
		Mockito.verify(this.baseDao).persist(foodSpecialty);

		Mockito.verifyZeroInteractions(this.validator, this.messageSource);
		Mockito.verifyNoMoreInteractions(foodSpecialty, this.baseDao);

	}

	@Test
	public void createAuthorityShouldInvokePersistence() throws Throwable {

		// Variables
		Authority authority;
		String code;
		List<Authority> results;

		// Given
		code = "BLABLA";
		results = null;
		LocaleContextHolder.setLocale(Locale.FRENCH);
		authority = Mockito.mock(Authority.class);

		// When
		Mockito.when(authority.getId()).thenReturn(2L);
		Mockito.when(authority.getCode()).thenReturn(code);
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
		this.underTest.createAuthority(authority);

		// Then
		Mockito.verify(authority, Mockito.times(1)).getId();
		Mockito.verify(authority).getCode();
		Mockito.verify(this.baseDao).findByExample(Matchers.any(Authority.class));
		Mockito.verify(this.baseDao).persist(authority);

		Mockito.verifyZeroInteractions(this.validator, this.messageSource);
		Mockito.verifyNoMoreInteractions(authority, this.baseDao);

	}

	@Test(expected = IllegalArgumentException.class)
	public void createFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() throws Throwable {

		// Given
		final FoodSpecialty foodSpecialty = null;

		// When
		this.underTest.createFoodSpecialty(foodSpecialty);

	}

	@Test(expected = IllegalStateException.class)
	public void createFoodSpecialtyShouldThrowIllegalStateExceptionWithNullFoodSpecialtyId() throws Throwable {

		// Given
		final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);

		Mockito.when(foodSpecialty.getId()).thenReturn(null);

		// When
		this.underTest.createFoodSpecialty(foodSpecialty);

	}

	@Test
	public void createRestaurantShouldInvokePersistence() {

		// Given
		final Restaurant restaurant = Mockito.mock(Restaurant.class);

		// When
		this.underTest.createRestaurant(restaurant);

		// Then
		Mockito.verify(this.baseDao).persist(restaurant);

		Mockito.verify(restaurant).getId();

	}

	// @Test(expected = IllegalArgumentException.class)
	// public void
	// createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {
	//
	// // Given
	// final Restaurant restaurant = null;
	//
	// // When
	// this.underTest.createRestaurant(restaurant);
	//
	// }

	@Test
	public void deleteFoodSpecialtyShouldInvokePersistence() {

		// Given
		final Long foodSpecialtyId = 5L;

		// When
		this.underTest.deleteFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(this.baseDao).delete(FoodSpecialty.class, foodSpecialtyId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

		// Given
		final Long foodSpecialtyId = null;

		// When
		this.underTest.deleteFoodSpecialty(foodSpecialtyId);

	}

	@Test
	public void deleteRestaurantShouldInvokePersistence() {

		// Given
		final Long restaurantId = 5L;
		final Long userId = 5L;

		// When
		User user = Mockito.mock(User.class);
		Mockito.when(this.baseDao.get(User.class, userId)).thenReturn(user);
		this.underTest.deleteRestaurant(userId, restaurantId);

		// Then
		Mockito.verify(this.baseDao).get(User.class, userId);
		Mockito.verify(user).removeRestaurant(restaurantId);
		Mockito.verifyNoMoreInteractions(this.baseDao, user);

	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long restaurantId = null;

		// When
		this.underTest.deleteRestaurant(null, restaurantId);

	}

	@Test
	public void findRestaurantByCriteriaShouldInvokePersistence() {

		// Given
		final Restaurant restaurant = Mockito.mock(Restaurant.class);

		// When
		this.underTest.findRestaurantsByCriteria(restaurant);

		// Then
		Mockito.verify(this.baseDao).findByExample(restaurant);

	}

	@Test(expected = IllegalArgumentException.class)
	public void findRestaurantByCriteriaShouldThrowIllegalArgumentExceptionWithNullRestaurant() {

		// Given
		final Restaurant restaurant = null;

		// When
		this.underTest.findRestaurantsByCriteria(restaurant);

	}

	@Test
	public void getFoodSpecialtyShouldInvokePersistence() {

		// Given
		final Long foodSpecialtyId = 5L;

		// When
		this.underTest.readFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, foodSpecialtyId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void getFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

		// Given
		final Long foodSpecialtyId = null;

		// When
		this.underTest.readFoodSpecialty(foodSpecialtyId);

	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void listFoodSpecialtiesShouldInvokePersistence() {

		// When
		this.underTest.listFoodSpecialties();

		// Then
		Mockito.verify(this.baseDao).findAll(FoodSpecialty.class);

	}

	@Test
	public void readRestaurantShouldInvokePersistence() {

		// Given
		final Long restaurantId = 5L;

		// When
		this.underTest.readRestaurant(restaurantId);

		// Then
		Mockito.verify(this.baseDao).get(Restaurant.class, restaurantId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void readRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long restaurantId = null;

		// When
		this.underTest.readRestaurant(restaurantId);

	}

	@Test
	public void updateFoodSpecialtyShouldSetPropertiesThenInvokePersistence() {

		// Variables
		FoodSpecialty foodSpecialty;
		FoodSpecialty persistedInstance;
		String code;
		List<FoodSpecialty> results;
		String label;
		boolean active;
		Long id;

		// Given
		code = "BLABLA";
		results = null;
		LocaleContextHolder.setLocale(Locale.FRENCH);
		foodSpecialty = Mockito.mock(FoodSpecialty.class);
		persistedInstance = Mockito.mock(FoodSpecialty.class);
		label = "bla";
		active = true;
		id = 5L;

		// When
		Mockito.when(this.baseDao.get(FoodSpecialty.class, id)).thenReturn(persistedInstance);
		Mockito.when(foodSpecialty.getId()).thenReturn(id);
		Mockito.when(foodSpecialty.getCode()).thenReturn(code);
		Mockito.when(foodSpecialty.isActive()).thenReturn(active);
		Mockito.when(foodSpecialty.getLabel()).thenReturn(label);
		Mockito.when(this.baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
		this.underTest.updateFoodSpecialty(foodSpecialty);

		// Then
		Mockito.verify(persistedInstance).setActive(active);
		Mockito.verify(persistedInstance).setCode(code);
		Mockito.verify(persistedInstance).setLabel(label);

		Mockito.verify(foodSpecialty).getId();
		Mockito.verify(foodSpecialty, Mockito.times(2)).getCode();
		Mockito.verify(foodSpecialty).isActive();
		Mockito.verify(foodSpecialty).getLabel();
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, id);
		Mockito.verify(this.baseDao).findByExample(Matchers.any(FoodSpecialty.class));

		Mockito.verifyZeroInteractions(this.validator, this.messageSource);
		Mockito.verifyNoMoreInteractions(foodSpecialty, this.baseDao, persistedInstance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() {

		// Given
		final FoodSpecialty foodSpecialty = null;

		// When
		this.underTest.updateFoodSpecialty(foodSpecialty);

	}

	@Test(expected = IllegalArgumentException.class)
	public void updateFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

		// Given
		final FoodSpecialty foodSpecialty = new FoodSpecialty();

		// When
		this.underTest.updateFoodSpecialty(foodSpecialty);

	}

	@Test(expected = IllegalStateException.class)
	public void updateFoodSpecialtyShouldThrowIllegalStateExceptionWithNullPersistedInstance() {

		// Given
		final FoodSpecialty foodSpecialty = new FoodSpecialty();

		foodSpecialty.setId(4L);

		Mockito.when(this.baseDao.get(Matchers.eq(FoodSpecialty.class), Matchers.anyLong())).thenReturn(null);

		// When
		this.underTest.updateFoodSpecialty(foodSpecialty);

	}

	@Test
	public void updateRestaurantShouldSetPropertiesThenInvokePersistence() {

		// Given
		final Restaurant restaurant = Mockito.mock(Restaurant.class);

		final Restaurant persistedInstance = Mockito.mock(Restaurant.class);

		Mockito.when(this.baseDao.get(Matchers.eq(Restaurant.class), Matchers.any(Long.class))).thenReturn(
				persistedInstance);

		// When
		this.underTest.updateRestaurant(restaurant);

		// Then
		Mockito.verify(persistedInstance).setAddress(restaurant.getAddress());

		Mockito.verify(persistedInstance).setDescription(restaurant.getDescription());

		Mockito.verify(persistedInstance).setCompanyId(restaurant.getCompanyId());

		Mockito.verify(persistedInstance).setKosher(restaurant.isKosher());

		Mockito.verify(persistedInstance).setHalal(restaurant.isHalal());

		Mockito.verify(persistedInstance).setVegetarian(restaurant.isVegetarian());

		Mockito.verify(persistedInstance).setMainOffer(restaurant.getMainOffer());

		Mockito.verify(persistedInstance).setName(restaurant.getName());

		Mockito.verify(persistedInstance).setPhoneNumber(restaurant.getPhoneNumber());

		Mockito.verify(persistedInstance).setSpecialties(restaurant.getSpecialties());

	}

	@Test(expected = IllegalArgumentException.class)
	public void updateRestaurantShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() {

		// Given
		final Restaurant restaurant = null;

		// When
		this.underTest.updateRestaurant(restaurant);

	}

	@Test(expected = IllegalArgumentException.class)
	public void updateRestaurantShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

		// Given
		final Restaurant restaurant = new Restaurant();

		// When
		this.underTest.updateRestaurant(restaurant);

	}

	@Test(expected = IllegalStateException.class)
	public void updateRestaurantShouldThrowIllegalStateExceptionWithNullPersistedInstance() {

		// Given
		final Restaurant restaurant = new Restaurant();

		restaurant.setId(3L);

		Mockito.when(this.baseDao.get(Matchers.eq(Restaurant.class), Matchers.any(Long.class))).thenReturn(null);

		// When
		this.underTest.updateRestaurant(restaurant);

	}

	@Test
	public void validateShouldNotThrowExceptionWithEmptyViolationsSet() {
		// Given
		final Restaurant toBeValidated = new Restaurant();
		final ValidationContext validationContext = ValidationContext.CREATE;
		final Set<ConstraintViolation<Persistable>> violations = null;
		Mockito.when(this.validator.validate(Matchers.any(Persistable.class), Matchers.any(Class[].class))).thenReturn(
				violations);

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = ConstraintViolationException.class)
	public void validateShouldThrowExceptionWithNonEmptyViolationsSet() {
		// Given
		final Restaurant toBeValidated = new Restaurant();
		final ValidationContext validationContext = ValidationContext.CREATE;
		final Set<ConstraintViolation<Persistable>> violations = new HashSet<ConstraintViolation<Persistable>>();
		violations.add(new ConstraintViolationImpl<Persistable>("{message.template}", "interpolated message",
				Persistable.class, null, String.class, null, null, null, null));
		Mockito.when(this.validator.validate(Matchers.any(Persistable.class), Matchers.any(Class[].class))).thenReturn(
				violations);

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateShouldThrowIllegalArgumentExceptionWithNullContext() {
		// Given
		final Restaurant toBeValidated = new Restaurant();
		final ValidationContext validationContext = null;

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateShouldThrowIllegalArgumentExceptionWithNullObject() {
		// Given
		final Restaurant toBeValidated = null;
		final ValidationContext validationContext = ValidationContext.DELETE;

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		User account = null;

		// When
		this.underTest.createAccount(account);
	}

	public void createAccountShouldInvokeBaseDao() {
		// Given
		User user = Mockito.mock(User.class);

		// When
		this.underTest.createAccount(user);

		Mockito.verify(this.baseDao).persist(user);
		Mockito.verify(user).getId();
		Mockito.verifyNoMoreInteractions(this.baseDao, user);
	}

	@Test(expected = IllegalStateException.class)
	public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFound() {
		// Given
		User user = new User();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(null);
		this.underTest.createAccount(user);
	}

	@Test(expected = IllegalStateException.class)
	public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFoundMoreThantOnce() {
		// Given
		User user = new User();
		List<Authority> foundAuthorities = new ArrayList<Authority>();
		foundAuthorities.add(new Authority());
		foundAuthorities.add(new Authority());
		foundAuthorities.add(new Authority());

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
		this.underTest.createAccount(user);
	}

	@Test
	public void createShouldSucceed() {
		// Given
		User user = Mockito.mock(User.class);
		List<Authority> foundAuthorities = new ArrayList<Authority>();

		Authority expectedAuthority = new Authority();
		foundAuthorities.add(expectedAuthority);

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
		this.underTest.createAccount(user);

		// Then
		// Should find by code default Authority
		Mockito.verify(this.baseDao).findByExample(Matchers.any(Authority.class));
		// Should clear before adding default Authority
		Mockito.verify(user).getEmail();
		Mockito.verify(user).clearAuthorities();
		// Should add default Authority
		Mockito.verify(user).addAuthority(expectedAuthority);
		// Should persist user
		Mockito.verify(this.baseDao).persist(user);
		// Should return id
		Mockito.verify(user).getId();
		Mockito.verifyNoMoreInteractions(this.baseDao, user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void readUserShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		Long id = null;

		// When
		this.underTest.readAccount(id);
	}

	public void readUserShouldInvokeBaseDao() {
		// Given
		Long id = 5L;
		User expectedUser = Mockito.mock(User.class);
		// When
		Mockito.when(this.baseDao.get(User.class, id)).thenReturn(expectedUser);
		User actualUser = this.underTest.readAccount(id);

		Mockito.verify(this.baseDao).get(User.class, id);
		Assert.assertSame(expectedUser, actualUser);
		Mockito.verifyNoMoreInteractions(this.baseDao);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		User user = null;

		// When
		this.underTest.updateAccount(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAccountShouldThrowIllegalArgumentExceptionWithNullIdentifier() {
		// Given
		User user = new User();

		// When
		this.underTest.updateAccount(user);
	}

	@Test(expected = IllegalStateException.class)
	public void updateAccountShouldThrowIllegalStateExceptionWhenNoUserFoundWithGivenId() {
		// Given
		User user = new User();
		Long id = 5L;
		user.setId(id);
		Mockito.when(this.baseDao.get(User.class, id)).thenReturn(null);

		// When
		this.underTest.updateAccount(user);
	}

	@Test
	public void updateAccountShouldReadAccountFromRepositoryAndMerge() {
		// Given
		User user = new User();
		Long id = 5L;
		user.setId(id);
		Mockito.when(this.baseDao.get(User.class, id)).thenReturn(new User());

		// When
		this.underTest.updateAccount(user);
		Mockito.verify(this.baseDao).get(User.class, id);
		Mockito.verify(this.baseDao).merge(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullUserId() {
		Long userId = null;
		Restaurant restaurant = new Restaurant();
		this.underTest.createRestaurant(userId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {
		Long userId = 5L;
		Restaurant restaurant = null;
		this.underTest.createRestaurant(userId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNonNullRestaurantId() {
		Long userId = 5L;
		Restaurant restaurant = new Restaurant();
		restaurant.setId(45L);
		this.underTest.createRestaurant(userId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldLoadUserThenAddRestaurantToItsRestaurantCollection() {
		Long userId = 5L;
		Restaurant restaurant = Mockito.mock(Restaurant.class);
		User user = Mockito.mock(User.class);

		Mockito.when(this.baseDao.get(User.class, userId)).thenReturn(user);
		this.underTest.createRestaurant(userId, restaurant);

		Mockito.verify(this.baseDao).get(User.class, userId);
		Mockito.verify(user).addRestaurant(restaurant);
		Mockito.verify(this.baseDao).merge(user);
		Mockito.verify(restaurant).getId();

		Mockito.verifyNoMoreInteractions(this.baseDao, user, restaurant);
	}

	@Test
	public void inactivatFoodSpecialtyShouldSetActiveToFalse() {
		// Given
		Long foodSpecialtyId = 3L;
		FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

		// When
		this.underTest.inactivateFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(foodSpecialty).setActive(false);
	}

	@Test
	public void deleteAccountShouldInvokePersistence() {

		// Given
		final Long userId = 5L;

		// When
		this.underTest.deleteAccount(userId);

		// Then
		Mockito.verify(this.baseDao).delete(User.class, userId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteAccountShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long userId = null;

		// When
		this.underTest.deleteAccount(userId);

	}

	public void checkUniqueFoodSpecialtyCodeShouldIgnoreNullInput() {
		// Variables
		FoodSpecialty foodSpecialty;

		// Given
		foodSpecialty = null;

		// When
		((FacadeImpl) this.underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueFoodSpecialtyCodeShouldIgnoreEmptyCode() {
		// Variables
		FoodSpecialty foodSpecialty;
		String code;

		// Given
		foodSpecialty = new FoodSpecialty();
		code = null;
		foodSpecialty.setCode(code);

		// When
		((FacadeImpl) this.underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		foodSpecialty = new FoodSpecialty();
		code = "";
		foodSpecialty.setCode(code);

		// When
		((FacadeImpl) this.underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueFoodSpecialtyCodeShouldIgnoreEmptyResult() {
		// Variables
		FoodSpecialty foodSpecialty;
		String code;
		List<FoodSpecialty> results;

		// Given
		foodSpecialty = new FoodSpecialty();
		code = "BLABLA";
		foodSpecialty.setCode(code);
		results = null;

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		foodSpecialty = new FoodSpecialty();
		code = "BLABLA";
		foodSpecialty.setCode(code);
		results = Collections.emptyList();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	@Test(expected = BusinessException.class)
	public void checkUniqueFoodSpecialtyCodeShouldThrowBusinessException() {

		// Variables
		FoodSpecialty foodSpecialty;
		String code;
		List<FoodSpecialty> results;
		String messageCode;
		String message;

		// Given
		foodSpecialty = new FoodSpecialty();
		code = "BLABLA";
		foodSpecialty.setCode(code);
		results = Arrays.asList(TestUtils.validFoodSpecialty());
		LocaleContextHolder.setLocale(Locale.FRENCH);
		messageCode = "foodSpecialty.code.already.used";
		message = "Code already used";

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
		Mockito.when(
				this.messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
						Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
		((FacadeImpl) this.underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

		// Then
		Mockito.verify(this.baseDao).findByExample(Matchers.any(FoodSpecialty.class));
		Mockito.verify(this.messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
				Matchers.eq(LocaleContextHolder.getLocale()));
		Mockito.verifyZeroInteractions(this.validator);
		Mockito.verifyNoMoreInteractions(this.baseDao, this.messageSource);

	}

	public void checkUniqueAuthorityCodeShouldIgnoreNullInput() {
		// Variables
		Authority authority;

		// Given
		authority = null;

		// When
		((FacadeImpl) this.underTest).checkUniqueAuthorityCode(authority);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueAuthorityCodeShouldIgnoreEmptyCode() {
		// Variables
		Authority authority;
		String code;

		// Given
		authority = new Authority();
		code = null;
		authority.setCode(code);

		// When
		((FacadeImpl) this.underTest).checkUniqueAuthorityCode(authority);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		authority = new Authority();
		code = "";
		authority.setCode(code);

		// When
		((FacadeImpl) this.underTest).checkUniqueAuthorityCode(authority);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueAuthorityCodeShouldIgnoreEmptyResult() {
		// Variables
		Authority authority;
		String code;
		List<Authority> results;

		// Given
		authority = new Authority();
		code = "BLABLA";
		authority.setCode(code);
		results = null;

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueAuthorityCode(authority);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		authority = new Authority();
		code = "BLABLA";
		authority.setCode(code);
		results = Collections.emptyList();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueAuthorityCode(authority);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	@Test(expected = BusinessException.class)
	public void checkUniqueAuthorityCodeShouldThrowBusinessException() {

		// Variables
		Authority authority;
		String code;
		List<Authority> results;
		String messageCode;
		String message;

		// Given
		authority = new Authority();
		code = "BLABLA";
		authority.setCode(code);
		results = Arrays.asList(TestUtils.validAuthority());
		LocaleContextHolder.setLocale(Locale.FRENCH);
		messageCode = "authority.code.already.used";
		message = "Code already used";

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
		Mockito.when(
				this.messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
						Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
		((FacadeImpl) this.underTest).checkUniqueAuthorityCode(authority);

		// Then
		Mockito.verify(this.baseDao).findByExample(Matchers.any(Authority.class));
		Mockito.verify(this.messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
				Matchers.eq(LocaleContextHolder.getLocale()));
		Mockito.verifyZeroInteractions(this.validator);
		Mockito.verifyNoMoreInteractions(this.baseDao, this.messageSource);

	}

	public void checkUniqueAccountUIDShouldIgnoreNullInput() {
		// Variables
		User account;

		// Given
		account = null;

		// When
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueAccountUIDShouldIgnoreEmptyUID() {
		// Variables
		User account;
		String email;

		// Given
		account = new User();
		email = null;
		account.setEmail(email);

		// When
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		account = new User();
		email = StringUtils.EMPTY;
		account.setEmail(email);

		// When
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueAccountUIDShouldIgnoreEmptyResult() {
		// Variables
		User account;
		String email;
		List<User> results;

		// Given
		account = new User();
		email = "BLABLA";
		account.setEmail(email);
		results = null;

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(User.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		account = new User();
		email = "BLABLA";
		account.setEmail(email);
		results = Collections.emptyList();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(User.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	@Test(expected = BusinessException.class)
	public void checkUniqueAccountUIDShouldThrowBusinessException() {

		// Variables
		User account;
		String email;
		List<User> results;
		String messageCode;
		String message;

		// Given
		account = new User();
		email = "mail@mail.com";
		account.setEmail(email);
		results = Arrays.asList(TestUtils.validUser());
		LocaleContextHolder.setLocale(Locale.FRENCH);
		messageCode = "account.email.already.used";
		message = "Email already used";

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(User.class))).thenReturn(results);
		Mockito.when(
				this.messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
						Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verify(this.baseDao).findByExample(Matchers.any(User.class));
		Mockito.verify(this.messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
				Matchers.eq(LocaleContextHolder.getLocale()));
		Mockito.verifyZeroInteractions(this.validator);
		Mockito.verifyNoMoreInteractions(this.baseDao, this.messageSource);

	}
}
