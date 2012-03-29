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
import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Persistable;
import fr.midipascher.domain.Restaurant;
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
		final Long accountId = 5L;

		// When
		Account account = Mockito.mock(Account.class);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		this.underTest.deleteRestaurant(accountId, restaurantId);

		// Then
		Mockito.verify(this.baseDao).get(Account.class, accountId);
		Mockito.verify(account).removeRestaurant(restaurantId);
		Mockito.verifyNoMoreInteractions(this.baseDao, account);

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
	public void readFoodSpecialtyShouldInvokePersistence() {

		// Given
		final Long foodSpecialtyId = 5L;
		FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

		// When
		this.underTest.readFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, foodSpecialtyId);

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
		final Long accountId = 5L;
		Account account;

		// When
		account = Mockito.mock(Account.class);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		this.underTest.readRestaurant(accountId, restaurantId, false);

		// Then
		Mockito.verify(this.baseDao).get(Restaurant.class, restaurantId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void readRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long restaurantId = null;
		final Long accountId = 5L;
		Account account;

		// When
		account = Mockito.mock(Account.class);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);

		// When
		this.underTest.readRestaurant(accountId, restaurantId, false);

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
		Account account = null;

		// When
		this.underTest.createAccount(account);
	}

	public void createAccountShouldInvokeBaseDao() {
		// Given
		Account account = Mockito.mock(Account.class);

		// When
		this.underTest.createAccount(account);

		Mockito.verify(this.baseDao).persist(account);
		Mockito.verify(account).getId();
		Mockito.verifyNoMoreInteractions(this.baseDao, account);
	}

	@Test(expected = IllegalStateException.class)
	public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFound() {
		// Given
		Account account = new Account();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(null);
		this.underTest.createAccount(account);
	}

	@Test(expected = IllegalStateException.class)
	public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFoundMoreThantOnce() {
		// Given
		Account account = new Account();
		List<Authority> foundAuthorities = new ArrayList<Authority>();
		foundAuthorities.add(new Authority());
		foundAuthorities.add(new Authority());
		foundAuthorities.add(new Authority());

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
		this.underTest.createAccount(account);
	}

	@Test
	public void createShouldSucceed() {
		// Given
		Account account = Mockito.mock(Account.class);
		List<Authority> foundAuthorities = new ArrayList<Authority>();

		Authority expectedAuthority = new Authority();
		foundAuthorities.add(expectedAuthority);

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
		this.underTest.createAccount(account);

		// Then
		// Should find by code default Authority
		Mockito.verify(this.baseDao).findByExample(Matchers.any(Authority.class));
		// Should clear before adding default Authority
		Mockito.verify(account).getEmail();
		Mockito.verify(account).clearAuthorities();
		// Should add default Authority
		Mockito.verify(account).addAuthority(expectedAuthority);
		// Should persist account
		Mockito.verify(this.baseDao).persist(account);
		// Should return id
		Mockito.verify(account).getId();
		Mockito.verifyNoMoreInteractions(this.baseDao, account);
	}

	@Test(expected = IllegalArgumentException.class)
	public void readUserShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		Long id = null;

		// When
		this.underTest.readAccount(id, false);
	}

	public void readUserShouldInvokeBaseDao() {
		// Given
		Long id = 5L;
		Account expectedUser = Mockito.mock(Account.class);
		// When
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(expectedUser);
		Account actualUser = this.underTest.readAccount(id, false);

		Mockito.verify(this.baseDao).get(Account.class, id);
		Assert.assertSame(expectedUser, actualUser);
		Mockito.verifyNoMoreInteractions(this.baseDao);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		Account account = null;

		// When
		this.underTest.updateAccount(account);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAccountShouldThrowIllegalArgumentExceptionWithNullIdentifier() {
		// Given
		Account account = new Account();

		// When
		this.underTest.updateAccount(account);
	}

	@Test(expected = IllegalStateException.class)
	public void updateAccountShouldThrowIllegalStateExceptionWhenNoUserFoundWithGivenId() {
		// Given
		Account account = new Account();
		Long id = 5L;
		account.setId(id);
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(null);

		// When
		this.underTest.updateAccount(account);
	}

	@Test
	public void updateAccountShouldReadAccountFromRepositoryAndMerge() {
		// Given
		Account account = new Account();
		Long id = 5L;
		account.setId(id);
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(new Account());

		// When
		this.underTest.updateAccount(account);
		Mockito.verify(this.baseDao).get(Account.class, id);
		Mockito.verify(this.baseDao).merge(account);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullUserId() {
		Long accountId = null;
		Restaurant restaurant = new Restaurant();
		this.underTest.createRestaurant(accountId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {
		Long accountId = 5L;
		Restaurant restaurant = null;
		this.underTest.createRestaurant(accountId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNonNullRestaurantId() {
		Long accountId = 5L;
		Restaurant restaurant = new Restaurant();
		restaurant.setId(45L);
		this.underTest.createRestaurant(accountId, restaurant);
	}

	@Test(expected = BusinessException.class)
	public void createRestaurantShouldThrowBusinessExceptionWithUnknownUser() {

		Long accountId = 5L;
		Long restaurantId = null;
		Restaurant restaurant = Mockito.mock(Restaurant.class);
		Account account = null;

		Mockito.when(restaurant.getId()).thenReturn(restaurantId);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		this.underTest.createRestaurant(accountId, restaurant);

		Mockito.verify(this.baseDao).refresh(restaurant.getSpecialties());
		Mockito.verify(this.baseDao).get(Account.class, accountId);
		Mockito.verify(restaurant).getId();

		Mockito.verifyNoMoreInteractions(this.baseDao, account, restaurant);

	}

	@Test
	public void createRestaurantShouldLoadUserThenAddRestaurantToItsRestaurantCollection() {

		Long accountId;
		Long restaurantId = null;
		Restaurant restaurant = Mockito.mock(Restaurant.class);
		Account account = Mockito.mock(Account.class);
		Set<FoodSpecialty> specialties;
		FoodSpecialty sp0;
		FoodSpecialty sp1;
		Long id0;

		accountId = 5L;
		restaurantId = null;
		restaurant = Mockito.mock(Restaurant.class);
		account = Mockito.mock(Account.class);
		restaurant = Mockito.mock(Restaurant.class);
		sp0 = Mockito.mock(FoodSpecialty.class);
		sp1 = Mockito.mock(FoodSpecialty.class);
		id0 = 5L;

		Mockito.when(sp0.getId()).thenReturn(id0);
		specialties = new HashSet<FoodSpecialty>(Arrays.asList(sp0));
		Mockito.when(restaurant.getSpecialties()).thenReturn(specialties);
		Mockito.when((this.baseDao).get(FoodSpecialty.class, id0)).thenReturn(sp1);

		Mockito.when(restaurant.getId()).thenReturn(restaurantId);
		Mockito.when(restaurant.getSpecialties()).thenReturn(specialties);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);

		this.underTest.createRestaurant(accountId, restaurant);

		Mockito.verify(this.baseDao).get(Account.class, accountId);
		// Mockito.verify(restaurant).getSpecialties();
		// Mockito.verify(restaurant).clearSpecialties();
		// Mockito.verify(this.baseDao).get(FoodSpecialty.class, id0);
		// Mockito.verify(restaurant).addSpecialty(sp1);

		Mockito.verify(account).addRestaurant(restaurant);
		Mockito.verify(this.baseDao).persist(restaurant);
		Mockito.verify(restaurant, Mockito.times(2)).getId();
		// Mockito.verify(restaurant).countSpecialties();

		Mockito.verifyNoMoreInteractions(this.baseDao, account, restaurant);

	}

	//
	// @Test
	// public void attachPersitentFoodSpecialtiesShouldAddSpecialties() {
	//
	// // Variables
	// Restaurant detachedRestaurant;
	// Set<FoodSpecialty> specialties;
	// FoodSpecialty sp0;
	// FoodSpecialty sp1;
	// Long id0;
	// Long id1;
	// FoodSpecialty sp2;
	//
	// // Given
	// detachedRestaurant = Mockito.mock(Restaurant.class);
	// sp0 = Mockito.mock(FoodSpecialty.class);
	// sp1 = Mockito.mock(FoodSpecialty.class);
	// sp2 = Mockito.mock(FoodSpecialty.class);
	// id0 = 5L;
	// id1 = 75L;
	// Mockito.when(sp0.getId()).thenReturn(id0);
	// Mockito.when(sp1.getId()).thenReturn(id1);
	// specialties = new HashSet<FoodSpecialty>(Arrays.asList(sp0, sp1));
	// Mockito.when(detachedRestaurant.getSpecialties()).thenReturn(specialties);
	// Mockito.when((this.baseDao).get(FoodSpecialty.class,
	// id0)).thenReturn(sp2);
	// Mockito.when((this.baseDao).get(FoodSpecialty.class,
	// id1)).thenReturn(null);
	//
	// // When
	// ((FacadeImpl)
	// this.underTest).attachPersistentFoodSpecialties(detachedRestaurant);
	//
	// // Then
	// Mockito.verify(detachedRestaurant).getSpecialties();
	// Mockito.verify(detachedRestaurant).clearSpecialties();
	// Mockito.verify(this.baseDao).get(FoodSpecialty.class, id0);
	// Mockito.verify(this.baseDao).get(FoodSpecialty.class, id1);
	// Mockito.verify(detachedRestaurant).addSpecialty(sp2);
	// Mockito.verify(detachedRestaurant).countSpecialties();
	// Mockito.verifyNoMoreInteractions(detachedRestaurant);
	// Mockito.verifyNoMoreInteractions(this.baseDao);
	//
	// }
	//
	// @Test
	// public void attachPersitentFoodSpecialtiesShouldNotAddSpecialties() {
	//
	// // Variables
	// Restaurant detachedRestaurant;
	// Set<FoodSpecialty> specialties;
	//
	// // Given
	// detachedRestaurant = Mockito.mock(Restaurant.class);
	// specialties = null;
	// Mockito.when(detachedRestaurant.getSpecialties()).thenReturn(specialties);
	//
	// // When
	// ((FacadeImpl)
	// this.underTest).attachPersistentFoodSpecialties(detachedRestaurant);
	//
	// // Then
	// Mockito.verify(detachedRestaurant).getSpecialties();
	// Mockito.verifyNoMoreInteractions(detachedRestaurant);
	//
	// // Given
	// Mockito.reset(detachedRestaurant);
	// specialties = new HashSet<FoodSpecialty>();
	// Mockito.when(detachedRestaurant.getSpecialties()).thenReturn(specialties);
	//
	// // When
	// ((FacadeImpl)
	// this.underTest).attachPersistentFoodSpecialties(detachedRestaurant);
	//
	// // Then
	// Mockito.verify(detachedRestaurant).getSpecialties();
	// Mockito.verifyNoMoreInteractions(detachedRestaurant);
	// Mockito.verifyZeroInteractions(this.baseDao);
	//
	// }

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
		final Long accountId = 5L;

		// When
		this.underTest.deleteAccount(accountId);

		// Then
		Mockito.verify(this.baseDao).delete(Account.class, accountId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteAccountShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long accountId = null;

		// When
		this.underTest.deleteAccount(accountId);

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
		Account account;

		// Given
		account = null;

		// When
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueAccountUIDShouldIgnoreEmptyUID() {
		// Variables
		Account account;
		String email;

		// Given
		account = new Account();
		email = null;
		account.setEmail(email);

		// When
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		account = new Account();
		email = StringUtils.EMPTY;
		account.setEmail(email);

		// When
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	public void checkUniqueAccountUIDShouldIgnoreEmptyResult() {
		// Variables
		Account account;
		String email;
		List<Account> results;

		// Given
		account = new Account();
		email = "BLABLA";
		account.setEmail(email);
		results = null;

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Account.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

		// Given
		account = new Account();
		email = "BLABLA";
		account.setEmail(email);
		results = Collections.emptyList();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Account.class))).thenReturn(results);
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verifyZeroInteractions(this.validator, this.baseDao, this.messageSource);

	}

	@Test(expected = BusinessException.class)
	public void checkUniqueAccountUIDShouldThrowBusinessException() {

		// Variables
		Account account;
		String email;
		List<Account> results;
		String messageCode;
		String message;

		// Given
		account = new Account();
		email = "mail@mail.com";
		account.setEmail(email);
		results = Arrays.asList(TestUtils.validUser());
		LocaleContextHolder.setLocale(Locale.FRENCH);
		messageCode = "account.email.already.used";
		message = "Email already used";

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Account.class))).thenReturn(results);
		Mockito.when(
				this.messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
						Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
		((FacadeImpl) this.underTest).checkUniqueAccountUID(account);

		// Then
		Mockito.verify(this.baseDao).findByExample(Matchers.any(Account.class));
		Mockito.verify(this.messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
				Matchers.eq(LocaleContextHolder.getLocale()));
		Mockito.verifyZeroInteractions(this.validator);
		Mockito.verifyNoMoreInteractions(this.baseDao, this.messageSource);

	}

	@Test(expected = BusinessException.class)
	public void readFoodSpecialtyShouldThrowBusinessExceptionWhithNullId() {
		this.underTest.readFoodSpecialty(null);
	}

	@Test(expected = BusinessException.class)
	public void readFoodSpecialtyShouldThrowBusinessExceptionWhenNotFound() {

		// Given
		final Long foodSpecialtyId = 5L;
		FoodSpecialty foodSpecialty = null;
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

		// When
		this.underTest.readFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, foodSpecialtyId);
		Mockito.verifyNoMoreInteractions(this.baseDao);

	}
}
