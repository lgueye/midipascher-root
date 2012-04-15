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

import org.apache.commons.lang3.StringUtils;
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

import com.google.common.collect.Sets;

import fr.midipascher.business.impl.FacadeImpl;
import fr.midipascher.domain.Account;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.business.Validator;
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
		results = Arrays.asList(TestUtils.validAccount());
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

	public void createAccountShouldInvokeBaseDao() {
		// Given
		final Account account = Mockito.mock(Account.class);

		// When
		this.underTest.createAccount(account);

		Mockito.verify(this.baseDao).persist(account);
		Mockito.verify(this.validator).validate(account, ValidationContext.CREATE);
		Mockito.verify(account).getId();
		Mockito.verifyZeroInteractions(this.messageSource);
		Mockito.verifyNoMoreInteractions(this.validator, account, this.baseDao);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		final Account account = null;

		// When
		this.underTest.createAccount(account);
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
		Mockito.verify(this.validator).validate(authority, ValidationContext.CREATE);

		Mockito.verifyZeroInteractions(this.messageSource);
		Mockito.verifyNoMoreInteractions(this.validator, authority, this.baseDao);

	}

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
		Mockito.verify(this.validator).validate(foodSpecialty, ValidationContext.CREATE);
		Mockito.verifyZeroInteractions(this.messageSource);
		Mockito.verifyNoMoreInteractions(this.validator, foodSpecialty, this.baseDao);

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
		Mockito.when(this.baseDao.get(FoodSpecialty.class, id0)).thenReturn(sp1);

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
		Mockito.verify(this.validator).validate(restaurant, ValidationContext.CREATE);
		Mockito.verify(this.baseDao).persist(restaurant);
		Mockito.verify(restaurant, Mockito.times(2)).getId();
		// Mockito.verify(restaurant).countSpecialties();

		Mockito.verifyNoMoreInteractions(this.baseDao, account, restaurant, this.validator);
		Mockito.verifyZeroInteractions(this.messageSource);

	}

	@Test(expected = BusinessException.class)
	public void createRestaurantShouldThrowBusinessExceptionWithUnknownUser() {

		final Long accountId = 5L;
		final Long restaurantId = null;
		final Restaurant restaurant = Mockito.mock(Restaurant.class);
		final Account account = null;

		Mockito.when(restaurant.getId()).thenReturn(restaurantId);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		this.underTest.createRestaurant(accountId, restaurant);

		Mockito.verify(this.baseDao).refresh(restaurant.getSpecialties());
		Mockito.verify(this.baseDao).get(Account.class, accountId);
		Mockito.verify(restaurant).getId();

		Mockito.verifyNoMoreInteractions(this.baseDao, account, restaurant);

	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNonNullRestaurantId() {
		final Long accountId = 5L;
		final Restaurant restaurant = new Restaurant();
		restaurant.setId(45L);
		this.underTest.createRestaurant(accountId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {
		final Long accountId = 5L;
		final Restaurant restaurant = null;
		this.underTest.createRestaurant(accountId, restaurant);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullUserId() {
		final Long accountId = null;
		final Restaurant restaurant = new Restaurant();
		this.underTest.createRestaurant(accountId, restaurant);
	}

	@Test
	public void createShouldSucceed() {
		// Given
		final Account account = Mockito.mock(Account.class);
		final List<Authority> foundAuthorities = new ArrayList<Authority>();

		final Authority expectedAuthority = new Authority();
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

	@Test(expected = IllegalStateException.class)
	public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFound() {
		// Given
		final Account account = new Account();

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(null);
		this.underTest.createAccount(account);
	}

	@Test(expected = IllegalStateException.class)
	public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFoundMoreThantOnce() {
		// Given
		final Account account = new Account();
		final List<Authority> foundAuthorities = new ArrayList<Authority>();
		foundAuthorities.add(new Authority());
		foundAuthorities.add(new Authority());
		foundAuthorities.add(new Authority());

		// When
		Mockito.when(this.baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
		this.underTest.createAccount(account);
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
		final Account account = Mockito.mock(Account.class);
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
	public void inactivatFoodSpecialtyShouldSetActiveToFalse() {
		// Given
		final Long foodSpecialtyId = 3L;
		final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

		// When
		this.underTest.inactivateFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(foodSpecialty).setActive(false);
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
	public void readFoodSpecialtyShouldInvokePersistence() {

		// Given
		final Long foodSpecialtyId = 5L;
		final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

		// When
		this.underTest.readFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, foodSpecialtyId);

	}

	@Test(expected = BusinessException.class)
	public void readFoodSpecialtyShouldThrowBusinessExceptionWhenNotFound() {

		// Given
		final Long foodSpecialtyId = 5L;
		final FoodSpecialty foodSpecialty = null;
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

		// When
		this.underTest.readFoodSpecialty(foodSpecialtyId);

		// Then
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, foodSpecialtyId);
		Mockito.verifyNoMoreInteractions(this.baseDao);

	}

	@Test(expected = BusinessException.class)
	public void readFoodSpecialtyShouldThrowBusinessExceptionWhithNullId() {
		this.underTest.readFoodSpecialty(null);
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
		Restaurant persisted = Mockito.mock(Restaurant.class);
		Mockito.when(this.baseDao.get(Restaurant.class, restaurantId)).thenReturn(persisted);

		this.underTest.readRestaurant(accountId, restaurantId, false);

		// Then
		Mockito.verify(this.baseDao).get(Restaurant.class, restaurantId);

	}

	@Test(expected = BusinessException.class)
	public void readRestaurantShouldThrowBusinessExceptionWithNullId() {

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
	public void readAccountShouldInvokeBaseDao() {
		// Variables
		final Long id;
		final Account expectedUser;

		// Given
		id = 5L;
		expectedUser = Mockito.mock(Account.class);
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(expectedUser);

		// When
		final Account actualUser = this.underTest.readAccount(id, false);

		// Then
		Mockito.verify(this.baseDao).get(Account.class, id);
		Assert.assertSame(expectedUser, actualUser);
		Mockito.verifyNoMoreInteractions(this.baseDao);
	}

	@Test
	public void updateAccountShouldReadAccountFromRepositoryAndMerge() {

		// Variables
		final Account detached;
		final Long id;
		String email;
		String firstName;
		String lastName;
		String password;
		// Set<Restaurant> restaurants;

		// Given
		detached = Mockito.mock(Account.class);
		id = 5L;
		email = "mail@mail.com";
		firstName = "xavier";
		lastName = "dcx";
		password = "secret";
		// restaurants = null;
		Account persisted = Mockito.mock(Account.class);
		Mockito.when(detached.getId()).thenReturn(id);
		Mockito.when(detached.getEmail()).thenReturn(email);
		Mockito.when(detached.getFirstName()).thenReturn(firstName);
		Mockito.when(detached.getLastName()).thenReturn(lastName);
		Mockito.when(detached.getPassword()).thenReturn(password);
		// Mockito.when(account.getRestaurants()).thenReturn(restaurants);
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(persisted);

		// When
		this.underTest.updateAccount(id, detached);

		// Then
		Mockito.verify(this.baseDao).get(Account.class, id);
		Mockito.verify(this.baseDao).findByExample(Matchers.any(Account.class));
		Mockito.verify(detached, Mockito.times(2)).getEmail();
		Mockito.verify(detached).getFirstName();
		Mockito.verify(detached).getLastName();
		Mockito.verify(detached).getPassword();
		// Mockito.verify(account).getRestaurants();
		Mockito.verify(persisted).setEmail(email);
		Mockito.verify(persisted).setFirstName(firstName);
		Mockito.verify(persisted).setLastName(lastName);
		Mockito.verify(persisted).setPassword(password);
		// Mockito.verify(persistedAccount).setRestaurants(restaurants);
		Mockito.verify(this.validator).validate(persisted, ValidationContext.UPDATE);
		Mockito.verifyNoMoreInteractions(detached, persisted, this.baseDao);
		Mockito.verifyZeroInteractions(this.baseDao);

	}

	@Test(expected = BusinessException.class)
	public void updateAccountShouldThrowBusinessExceptionWithNullIdentifier() {
		// Variables
		final Long id;
		final Account account;

		// Given
		id = 5L;
		account = new Account();

		// When
		this.underTest.updateAccount(id, account);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
		// Variables
		final Long id;
		final Account account;

		// Given
		id = 5L;
		account = null;

		// When
		this.underTest.updateAccount(id, account);
	}

	@Test(expected = BusinessException.class)
	public void updateAccountShouldThrowBusinessExceptionWhenNoUserFoundWithGivenId() {

		// Variables
		final Account account;
		final Long id;

		// Given
		account = new Account();
		id = 5L;
		account.setId(id);
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(null);

		// When
		this.underTest.updateAccount(id, account);
	}

	@Test
	public void updateFoodSpecialtyShouldSetPropertiesThenInvokePersistence() {

		// Variables
		final FoodSpecialty detached;
		final Long id;
		boolean active;
		String code;
		String label;

		// Given
		detached = Mockito.mock(FoodSpecialty.class);
		id = 5L;
		active = true;
		code = "CODE";
		label = "label";
		FoodSpecialty persisted = Mockito.mock(FoodSpecialty.class);
		Mockito.when(detached.getId()).thenReturn(id);
		Mockito.when(detached.isActive()).thenReturn(active);
		Mockito.when(detached.getCode()).thenReturn(code);
		Mockito.when(detached.getLabel()).thenReturn(label);
		Mockito.when(this.baseDao.get(FoodSpecialty.class, id)).thenReturn(persisted);

		// When
		this.underTest.updateFoodSpecialty(id, detached);

		// Then
		Mockito.verify(this.baseDao).get(FoodSpecialty.class, id);
		Mockito.verify(this.baseDao).findByExample(Matchers.any(FoodSpecialty.class));
		Mockito.verify(detached, Mockito.times(2)).getCode();
		Mockito.verify(detached).isActive();
		Mockito.verify(detached).getLabel();
		Mockito.verify(persisted).setActive(active);
		Mockito.verify(persisted).setCode(code);
		Mockito.verify(persisted).setLabel(label);
		Mockito.verify(this.validator).validate(persisted, ValidationContext.UPDATE);
		Mockito.verifyNoMoreInteractions(detached, persisted, this.baseDao);
		Mockito.verifyZeroInteractions(this.messageSource);
	}

	@Test(expected = BusinessException.class)
	public void updateFoodSpecialtyShouldThrowBusinessExceptionWithNullFoodSpecialtyId() {

		// Given
		final FoodSpecialty foodSpecialty = new FoodSpecialty();
		final Long foodSpecialtyId = null;

		// When
		this.underTest.updateFoodSpecialty(foodSpecialtyId, foodSpecialty);

	}

	@Test(expected = BusinessException.class)
	public void updateFoodSpecialtyShouldThrowBusinessExceptionWithNullPersistedInstance() {

		// Given
		final FoodSpecialty foodSpecialty = new FoodSpecialty();

		foodSpecialty.setId(4L);

		Mockito.when(this.baseDao.get(Matchers.eq(FoodSpecialty.class), Matchers.anyLong())).thenReturn(null);

		// When
		this.underTest.updateFoodSpecialty(null, foodSpecialty);

	}

	@Test(expected = IllegalArgumentException.class)
	public void updateFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() {

		// Given
		final FoodSpecialty foodSpecialty = null;

		// When
		this.underTest.updateFoodSpecialty(null, foodSpecialty);

	}

	// @Test
	// public void updateRestaurantShouldSetPropertiesThenInvokePersistence() {
	//
	// // Given
	// final Restaurant restaurant = Mockito.mock(Restaurant.class);
	//
	// final Restaurant persistedInstance = Mockito.mock(Restaurant.class);
	//
	// Mockito.when(this.baseDao.get(Matchers.eq(Restaurant.class),
	// Matchers.any(Long.class))).thenReturn(
	// persistedInstance);
	//
	// // When
	// this.underTest.updateRestaurant(restaurant);
	//
	// // Then
	// Mockito.verify(persistedInstance).setAddress(restaurant.getAddress());
	//
	// Mockito.verify(persistedInstance).setDescription(restaurant.getDescription());
	//
	// Mockito.verify(persistedInstance).setCompanyId(restaurant.getCompanyId());
	//
	// Mockito.verify(persistedInstance).setKosher(restaurant.isKosher());
	//
	// Mockito.verify(persistedInstance).setHalal(restaurant.isHalal());
	//
	// Mockito.verify(persistedInstance).setVegetarian(restaurant.isVegetarian());
	//
	// Mockito.verify(persistedInstance).setMainOffer(restaurant.getMainOffer());
	//
	// Mockito.verify(persistedInstance).setName(restaurant.getName());
	//
	// Mockito.verify(persistedInstance).setPhoneNumber(restaurant.getPhoneNumber());
	//
	// Mockito.verify(persistedInstance).setSpecialties(restaurant.getSpecialties());
	//
	// }

	@Test(expected = BusinessException.class)
	public void readAccountShouldThrowBusinessExceptionWithNullAccountId() {

		// Variables
		Long id;

		// Given
		id = null;

		// When
		this.underTest.readAccount(id, false);

		// Then
	}

	@Test(expected = BusinessException.class)
	public void readAccountShouldThrowBusinessExceptionWhenAccountWasNotFound() {

		// Variables
		Long id;
		Account account;

		// Given
		id = 5L;
		account = null;
		Mockito.when(this.baseDao.get(Account.class, id)).thenReturn(account);

		// When
		this.underTest.readAccount(id, false);

		// Then
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {

		// Variables
		Long accountId;
		Long restaurantId;
		Restaurant restaurant;

		// Given
		accountId = 8L;
		restaurantId = 6L;
		restaurant = null;

		// When
		this.underTest.updateRestaurant(accountId, restaurantId, restaurant);

	}

	@Test(expected = BusinessException.class)
	public void updateRestaurantShouldThrowBusinessExceptionWithNotFoundRestaurant() {

		// Variables
		Long accountId;
		Long restaurantId;
		Restaurant restaurant;
		Account account;
		Set<Restaurant> accountRestaurants;
		Restaurant persisted;

		// Given
		accountId = 8L;
		restaurantId = 6L;
		restaurant = Mockito.mock(Restaurant.class);
		account = Mockito.mock(Account.class);
		accountRestaurants = Sets.newHashSet();
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		Mockito.when(account.getRestaurants()).thenReturn(accountRestaurants);

		// When
		this.underTest.updateRestaurant(accountId, restaurantId, restaurant);

		// Given
		accountId = 8L;
		restaurantId = 7L;
		Long differentRestaurantId = 9L;
		restaurant = Mockito.mock(Restaurant.class);
		account = Mockito.mock(Account.class);
		accountRestaurants = Sets.newHashSet();
		persisted = Mockito.mock(Restaurant.class);
		accountRestaurants.add(persisted);
		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		Mockito.when(account.getRestaurants()).thenReturn(accountRestaurants);
		Mockito.when(persisted.getId()).thenReturn(differentRestaurantId);

		// When
		this.underTest.updateRestaurant(accountId, restaurantId, restaurant);

	}

	@Test
	public void updateRestaurantShouldSucceed() {

		// Variables
		Long accountId;
		Long restaurantId;
		Restaurant detached;
		Account account;
		Set<Restaurant> accountRestaurants;
		Restaurant persisted;
		FoodSpecialty foodSpecialty;
		Long foodSpecialtyId;
		Address address;
		String companyId;
		String description;
		boolean halal;
		boolean kosher;
		String mainOffer;
		String name;
		String phoneNumber;
		boolean vegetarian;

		// Given
		accountId = 8L;
		restaurantId = 7L;
		detached = Mockito.mock(Restaurant.class);
		account = Mockito.mock(Account.class);
		accountRestaurants = Sets.newHashSet();
		persisted = Mockito.mock(Restaurant.class);
		accountRestaurants.add(persisted);
		Set<FoodSpecialty> foodSpecialties = Sets.newHashSet();
		foodSpecialty = Mockito.mock(FoodSpecialty.class);
		foodSpecialties.add(foodSpecialty);
		foodSpecialtyId = 8L;
		address = Mockito.mock(Address.class);
		companyId = "555 9997 556 3333";
		description = "description";
		mainOffer = "main offer";
		name = "name";
		phoneNumber = "014589632157";
		halal = true;
		kosher = false;
		vegetarian = true;

		Mockito.when(this.baseDao.get(Account.class, accountId)).thenReturn(account);
		Mockito.when(account.getRestaurants()).thenReturn(accountRestaurants);
		Mockito.when(persisted.getId()).thenReturn(restaurantId);
		Mockito.when(foodSpecialty.getId()).thenReturn(foodSpecialtyId);
		Mockito.when(this.baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);
		Mockito.when(detached.getSpecialties()).thenReturn(foodSpecialties);
		Mockito.when(detached.getAddress()).thenReturn(address);
		Mockito.when(detached.getCompanyId()).thenReturn(companyId);
		Mockito.when(detached.getDescription()).thenReturn(description);
		Mockito.when(detached.getMainOffer()).thenReturn(mainOffer);
		Mockito.when(detached.getName()).thenReturn(name);
		Mockito.when(detached.getPhoneNumber()).thenReturn(phoneNumber);
		Mockito.when(detached.isHalal()).thenReturn(halal);
		Mockito.when(detached.isKosher()).thenReturn(kosher);
		Mockito.when(detached.isVegetarian()).thenReturn(vegetarian);

		// When
		this.underTest.updateRestaurant(accountId, restaurantId, detached);

		// Then
		Mockito.verify(persisted, Mockito.times(2)).getId();
		Mockito.verify(persisted).setAddress(address);
		Mockito.verify(persisted).setCompanyId(companyId);
		Mockito.verify(persisted).setDescription(description);
		Mockito.verify(persisted).setHalal(halal);
		Mockito.verify(persisted).setKosher(kosher);
		Mockito.verify(persisted).setMainOffer(mainOffer);
		Mockito.verify(persisted).setName(name);
		Mockito.verify(persisted).setPhoneNumber(phoneNumber);
		Mockito.verify(persisted).setVegetarian(vegetarian);
		Mockito.verify(persisted).clearSpecialties();
		Mockito.verify(persisted).addSpecialty(foodSpecialty);
		Mockito.verify(this.validator).validate(persisted, ValidationContext.UPDATE);
		Mockito.verifyNoMoreInteractions(persisted);

	}
}
