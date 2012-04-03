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

import javax.validation.Validator;

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

import fr.midipascher.business.impl.FacadeImpl;
import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
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
    private Validator validator;

    @Mock
    private BaseDao baseDao;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private final Facade underTest = new FacadeImpl();

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
        Mockito.when(baseDao.findByExample(Matchers.any(Account.class))).thenReturn(results);
        ((FacadeImpl) underTest).checkUniqueAccountUID(account);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

        // Given
        account = new Account();
        email = "BLABLA";
        account.setEmail(email);
        results = Collections.emptyList();

        // When
        Mockito.when(baseDao.findByExample(Matchers.any(Account.class))).thenReturn(results);
        ((FacadeImpl) underTest).checkUniqueAccountUID(account);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

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
        ((FacadeImpl) underTest).checkUniqueAccountUID(account);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

        // Given
        account = new Account();
        email = StringUtils.EMPTY;
        account.setEmail(email);

        // When
        ((FacadeImpl) underTest).checkUniqueAccountUID(account);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

    }

    public void checkUniqueAccountUIDShouldIgnoreNullInput() {
        // Variables
        Account account;

        // Given
        account = null;

        // When
        ((FacadeImpl) underTest).checkUniqueAccountUID(account);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

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
        Mockito.when(baseDao.findByExample(Matchers.any(Account.class))).thenReturn(results);
        Mockito.when(
            messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
                Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
        ((FacadeImpl) underTest).checkUniqueAccountUID(account);

        // Then
        Mockito.verify(baseDao).findByExample(Matchers.any(Account.class));
        Mockito.verify(messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
            Matchers.eq(LocaleContextHolder.getLocale()));
        Mockito.verifyZeroInteractions(validator);
        Mockito.verifyNoMoreInteractions(baseDao, messageSource);

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
        ((FacadeImpl) underTest).checkUniqueAuthorityCode(authority);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

        // Given
        authority = new Authority();
        code = "";
        authority.setCode(code);

        // When
        ((FacadeImpl) underTest).checkUniqueAuthorityCode(authority);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

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
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
        ((FacadeImpl) underTest).checkUniqueAuthorityCode(authority);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

        // Given
        authority = new Authority();
        code = "BLABLA";
        authority.setCode(code);
        results = Collections.emptyList();

        // When
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
        ((FacadeImpl) underTest).checkUniqueAuthorityCode(authority);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

    }

    public void checkUniqueAuthorityCodeShouldIgnoreNullInput() {
        // Variables
        Authority authority;

        // Given
        authority = null;

        // When
        ((FacadeImpl) underTest).checkUniqueAuthorityCode(authority);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

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
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
        Mockito.when(
            messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
                Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
        ((FacadeImpl) underTest).checkUniqueAuthorityCode(authority);

        // Then
        Mockito.verify(baseDao).findByExample(Matchers.any(Authority.class));
        Mockito.verify(messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
            Matchers.eq(LocaleContextHolder.getLocale()));
        Mockito.verifyZeroInteractions(validator);
        Mockito.verifyNoMoreInteractions(baseDao, messageSource);

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
        ((FacadeImpl) underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

        // Given
        foodSpecialty = new FoodSpecialty();
        code = "";
        foodSpecialty.setCode(code);

        // When
        ((FacadeImpl) underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

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
        Mockito.when(baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
        ((FacadeImpl) underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

        // Given
        foodSpecialty = new FoodSpecialty();
        code = "BLABLA";
        foodSpecialty.setCode(code);
        results = Collections.emptyList();

        // When
        Mockito.when(baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
        ((FacadeImpl) underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

    }

    public void checkUniqueFoodSpecialtyCodeShouldIgnoreNullInput() {
        // Variables
        FoodSpecialty foodSpecialty;

        // Given
        foodSpecialty = null;

        // When
        ((FacadeImpl) underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

        // Then
        Mockito.verifyZeroInteractions(validator, baseDao, messageSource);

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
        Mockito.when(baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
        Mockito.when(
            messageSource.getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
                Matchers.eq(LocaleContextHolder.getLocale()))).thenReturn(message);
        ((FacadeImpl) underTest).checkUniqueFoodSpecialtyCode(foodSpecialty);

        // Then
        Mockito.verify(baseDao).findByExample(Matchers.any(FoodSpecialty.class));
        Mockito.verify(messageSource).getMessage(Matchers.eq(messageCode), Matchers.any(Object[].class),
            Matchers.eq(LocaleContextHolder.getLocale()));
        Mockito.verifyZeroInteractions(validator);
        Mockito.verifyNoMoreInteractions(baseDao, messageSource);

    }

    public void createAccountShouldInvokeBaseDao() {
        // Given
        final Account account = Mockito.mock(Account.class);

        // When
        underTest.createAccount(account);

        Mockito.verify(baseDao).persist(account);
        Mockito.verify(account).getId();
        Mockito.verifyNoMoreInteractions(baseDao, account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
        // Given
        final Account account = null;

        // When
        underTest.createAccount(account);
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
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(results);
        underTest.createAuthority(authority);

        // Then
        Mockito.verify(authority, Mockito.times(1)).getId();
        Mockito.verify(authority).getCode();
        Mockito.verify(baseDao).findByExample(Matchers.any(Authority.class));
        Mockito.verify(baseDao).persist(authority);

        Mockito.verifyZeroInteractions(validator, messageSource);
        Mockito.verifyNoMoreInteractions(authority, baseDao);

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
        Mockito.when(baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
        underTest.createFoodSpecialty(foodSpecialty);

        // Then
        Mockito.verify(foodSpecialty, Mockito.times(2)).getId();
        Mockito.verify(foodSpecialty).getCode();
        Mockito.verify(baseDao).findByExample(Matchers.any(FoodSpecialty.class));
        Mockito.verify(baseDao).persist(foodSpecialty);

        Mockito.verifyZeroInteractions(validator, messageSource);
        Mockito.verifyNoMoreInteractions(foodSpecialty, baseDao);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() throws Throwable {

        // Given
        final FoodSpecialty foodSpecialty = null;

        // When
        underTest.createFoodSpecialty(foodSpecialty);

    }

    @Test(expected = IllegalStateException.class)
    public void createFoodSpecialtyShouldThrowIllegalStateExceptionWithNullFoodSpecialtyId() throws Throwable {

        // Given
        final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);

        Mockito.when(foodSpecialty.getId()).thenReturn(null);

        // When
        underTest.createFoodSpecialty(foodSpecialty);

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
        Mockito.when(baseDao.get(FoodSpecialty.class, id0)).thenReturn(sp1);

        Mockito.when(restaurant.getId()).thenReturn(restaurantId);
        Mockito.when(restaurant.getSpecialties()).thenReturn(specialties);
        Mockito.when(baseDao.get(Account.class, accountId)).thenReturn(account);

        underTest.createRestaurant(accountId, restaurant);

        Mockito.verify(baseDao).get(Account.class, accountId);
        // Mockito.verify(restaurant).getSpecialties();
        // Mockito.verify(restaurant).clearSpecialties();
        // Mockito.verify(this.baseDao).get(FoodSpecialty.class, id0);
        // Mockito.verify(restaurant).addSpecialty(sp1);

        Mockito.verify(account).addRestaurant(restaurant);
        Mockito.verify(baseDao).persist(restaurant);
        Mockito.verify(restaurant, Mockito.times(2)).getId();
        // Mockito.verify(restaurant).countSpecialties();

        Mockito.verifyNoMoreInteractions(baseDao, account, restaurant);

    }

    @Test(expected = BusinessException.class)
    public void createRestaurantShouldThrowBusinessExceptionWithUnknownUser() {

        final Long accountId = 5L;
        final Long restaurantId = null;
        final Restaurant restaurant = Mockito.mock(Restaurant.class);
        final Account account = null;

        Mockito.when(restaurant.getId()).thenReturn(restaurantId);
        Mockito.when(baseDao.get(Account.class, accountId)).thenReturn(account);
        underTest.createRestaurant(accountId, restaurant);

        Mockito.verify(baseDao).refresh(restaurant.getSpecialties());
        Mockito.verify(baseDao).get(Account.class, accountId);
        Mockito.verify(restaurant).getId();

        Mockito.verifyNoMoreInteractions(baseDao, account, restaurant);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createRestaurantShouldThrowIllegalArgumentExceptionWithNonNullRestaurantId() {
        final Long accountId = 5L;
        final Restaurant restaurant = new Restaurant();
        restaurant.setId(45L);
        underTest.createRestaurant(accountId, restaurant);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {
        final Long accountId = 5L;
        final Restaurant restaurant = null;
        underTest.createRestaurant(accountId, restaurant);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullUserId() {
        final Long accountId = null;
        final Restaurant restaurant = new Restaurant();
        underTest.createRestaurant(accountId, restaurant);
    }

    @Test
    public void createShouldSucceed() {
        // Given
        final Account account = Mockito.mock(Account.class);
        final List<Authority> foundAuthorities = new ArrayList<Authority>();

        final Authority expectedAuthority = new Authority();
        foundAuthorities.add(expectedAuthority);

        // When
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
        underTest.createAccount(account);

        // Then
        // Should find by code default Authority
        Mockito.verify(baseDao).findByExample(Matchers.any(Authority.class));
        // Should clear before adding default Authority
        Mockito.verify(account).getEmail();
        Mockito.verify(account).clearAuthorities();
        // Should add default Authority
        Mockito.verify(account).addAuthority(expectedAuthority);
        // Should persist account
        Mockito.verify(baseDao).persist(account);
        // Should return id
        Mockito.verify(account).getId();
        Mockito.verifyNoMoreInteractions(baseDao, account);
    }

    @Test(expected = IllegalStateException.class)
    public void createShouldThowIllegalStateExceptionIfRMGRAuthorityWasNotFound() {
        // Given
        final Account account = new Account();

        // When
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(null);
        underTest.createAccount(account);
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
        Mockito.when(baseDao.findByExample(Matchers.any(Authority.class))).thenReturn(foundAuthorities);
        underTest.createAccount(account);
    }

    @Test
    public void deleteAccountShouldInvokePersistence() {

        // Given
        final Long accountId = 5L;

        // When
        underTest.deleteAccount(accountId);

        // Then
        Mockito.verify(baseDao).delete(Account.class, accountId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteAccountShouldThrowIllegalArgumentExceptionWithNullId() {

        // Given
        final Long accountId = null;

        // When
        underTest.deleteAccount(accountId);

    }

    @Test
    public void deleteFoodSpecialtyShouldInvokePersistence() {

        // Given
        final Long foodSpecialtyId = 5L;

        // When
        underTest.deleteFoodSpecialty(foodSpecialtyId);

        // Then
        Mockito.verify(baseDao).delete(FoodSpecialty.class, foodSpecialtyId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

        // Given
        final Long foodSpecialtyId = null;

        // When
        underTest.deleteFoodSpecialty(foodSpecialtyId);

    }

    @Test
    public void deleteRestaurantShouldInvokePersistence() {

        // Given
        final Long restaurantId = 5L;
        final Long accountId = 5L;

        // When
        final Account account = Mockito.mock(Account.class);
        Mockito.when(baseDao.get(Account.class, accountId)).thenReturn(account);
        underTest.deleteRestaurant(accountId, restaurantId);

        // Then
        Mockito.verify(baseDao).get(Account.class, accountId);
        Mockito.verify(account).removeRestaurant(restaurantId);
        Mockito.verifyNoMoreInteractions(baseDao, account);

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

        // Given
        final Long restaurantId = null;

        // When
        underTest.deleteRestaurant(null, restaurantId);

    }

    @Test
    public void findRestaurantByCriteriaShouldInvokePersistence() {

        // Given
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        // When
        underTest.findRestaurantsByCriteria(restaurant);

        // Then
        Mockito.verify(baseDao).findByExample(restaurant);

    }

    @Test(expected = IllegalArgumentException.class)
    public void findRestaurantByCriteriaShouldThrowIllegalArgumentExceptionWithNullRestaurant() {

        // Given
        final Restaurant restaurant = null;

        // When
        underTest.findRestaurantsByCriteria(restaurant);

    }

    @Test
    public void inactivatFoodSpecialtyShouldSetActiveToFalse() {
        // Given
        final Long foodSpecialtyId = 3L;
        final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);
        Mockito.when(baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

        // When
        underTest.inactivateFoodSpecialty(foodSpecialtyId);

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
        underTest.listFoodSpecialties();

        // Then
        Mockito.verify(baseDao).findAll(FoodSpecialty.class);

    }

    @Test
    public void readFoodSpecialtyShouldInvokePersistence() {

        // Given
        final Long foodSpecialtyId = 5L;
        final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);
        Mockito.when(baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

        // When
        underTest.readFoodSpecialty(foodSpecialtyId);

        // Then
        Mockito.verify(baseDao).get(FoodSpecialty.class, foodSpecialtyId);

    }

    @Test(expected = BusinessException.class)
    public void readFoodSpecialtyShouldThrowBusinessExceptionWhenNotFound() {

        // Given
        final Long foodSpecialtyId = 5L;
        final FoodSpecialty foodSpecialty = null;
        Mockito.when(baseDao.get(FoodSpecialty.class, foodSpecialtyId)).thenReturn(foodSpecialty);

        // When
        underTest.readFoodSpecialty(foodSpecialtyId);

        // Then
        Mockito.verify(baseDao).get(FoodSpecialty.class, foodSpecialtyId);
        Mockito.verifyNoMoreInteractions(baseDao);

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

    @Test(expected = BusinessException.class)
    public void readFoodSpecialtyShouldThrowBusinessExceptionWhithNullId() {
        underTest.readFoodSpecialty(null);
    }

    @Test
    public void readRestaurantShouldInvokePersistence() {

        // Given
        final Long restaurantId = 5L;
        final Long accountId = 5L;
        Account account;

        // When
        account = Mockito.mock(Account.class);
        Mockito.when(baseDao.get(Account.class, accountId)).thenReturn(account);
        underTest.readRestaurant(accountId, restaurantId, false);

        // Then
        Mockito.verify(baseDao).get(Restaurant.class, restaurantId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void readRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

        // Given
        final Long restaurantId = null;
        final Long accountId = 5L;
        Account account;

        // When
        account = Mockito.mock(Account.class);
        Mockito.when(baseDao.get(Account.class, accountId)).thenReturn(account);

        // When
        underTest.readRestaurant(accountId, restaurantId, false);

    }

    public void readUserShouldInvokeBaseDao() {
        // Given
        final Long id = 5L;
        final Account expectedUser = Mockito.mock(Account.class);
        // When
        Mockito.when(baseDao.get(Account.class, id)).thenReturn(expectedUser);
        final Account actualUser = underTest.readAccount(id, false);

        Mockito.verify(baseDao).get(Account.class, id);
        Assert.assertSame(expectedUser, actualUser);
        Mockito.verifyNoMoreInteractions(baseDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readUserShouldThrowIllegalArgumentExceptionWithNullInput() {
        // Given
        final Long id = null;

        // When
        underTest.readAccount(id, false);
    }

    @Test
    public void updateAccountShouldReadAccountFromRepositoryAndMerge() {
        // Given
        final Account account = new Account();
        final Long id = 5L;
        account.setId(id);
        Mockito.when(baseDao.get(Account.class, id)).thenReturn(new Account());

        // When
        underTest.updateAccount(account);
        Mockito.verify(baseDao).get(Account.class, id);
        Mockito.verify(baseDao).merge(account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAccountShouldThrowIllegalArgumentExceptionWithNullIdentifier() {
        // Given
        final Account account = new Account();

        // When
        underTest.updateAccount(account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAccountShouldThrowIllegalArgumentExceptionWithNullInput() {
        // Given
        final Account account = null;

        // When
        underTest.updateAccount(account);
    }

    @Test(expected = IllegalStateException.class)
    public void updateAccountShouldThrowIllegalStateExceptionWhenNoUserFoundWithGivenId() {
        // Given
        final Account account = new Account();
        final Long id = 5L;
        account.setId(id);
        Mockito.when(baseDao.get(Account.class, id)).thenReturn(null);

        // When
        underTest.updateAccount(account);
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
        Mockito.when(baseDao.get(FoodSpecialty.class, id)).thenReturn(persistedInstance);
        Mockito.when(foodSpecialty.getCode()).thenReturn(code);
        Mockito.when(foodSpecialty.isActive()).thenReturn(active);
        Mockito.when(foodSpecialty.getLabel()).thenReturn(label);
        Mockito.when(baseDao.findByExample(Matchers.any(FoodSpecialty.class))).thenReturn(results);
        underTest.updateFoodSpecialty(id, foodSpecialty);

        // Then
        Mockito.verify(persistedInstance).setActive(active);
        Mockito.verify(persistedInstance).setCode(code);
        Mockito.verify(persistedInstance).setLabel(label);

        Mockito.verify(foodSpecialty, Mockito.times(2)).getCode();
        Mockito.verify(foodSpecialty).isActive();
        Mockito.verify(foodSpecialty).getLabel();
        Mockito.verify(baseDao).get(FoodSpecialty.class, id);
        Mockito.verify(baseDao).findByExample(Matchers.any(FoodSpecialty.class));
        Mockito.verify(validator).validate(persistedInstance, ValidationContext.UPDATE.getContext());

        Mockito.verifyZeroInteractions(messageSource);
        Mockito.verifyNoMoreInteractions(foodSpecialty, baseDao, persistedInstance);
    }

    @Test(expected = BusinessException.class)
    public void updateFoodSpecialtyShouldThrowBusinessExceptionWithNullFoodSpecialtyId() {

        // Given
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        final Long foodSpecialtyId = null;

        // When
        underTest.updateFoodSpecialty(foodSpecialtyId, foodSpecialty);

    }

    @Test(expected = BusinessException.class)
    public void updateFoodSpecialtyShouldThrowBusinessExceptionWithNullPersistedInstance() {

        // Given
        final FoodSpecialty foodSpecialty = new FoodSpecialty();

        foodSpecialty.setId(4L);

        Mockito.when(baseDao.get(Matchers.eq(FoodSpecialty.class), Matchers.anyLong())).thenReturn(null);

        // When
        underTest.updateFoodSpecialty(null, foodSpecialty);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() {

        // Given
        final FoodSpecialty foodSpecialty = null;

        // When
        underTest.updateFoodSpecialty(null, foodSpecialty);

    }

    @Test
    public void updateRestaurantShouldSetPropertiesThenInvokePersistence() {

        // Given
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Restaurant persistedInstance = Mockito.mock(Restaurant.class);

        Mockito.when(baseDao.get(Matchers.eq(Restaurant.class), Matchers.any(Long.class)))
                .thenReturn(persistedInstance);

        // When
        underTest.updateRestaurant(restaurant);

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
        underTest.updateRestaurant(restaurant);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRestaurantShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

        // Given
        final Restaurant restaurant = new Restaurant();

        // When
        underTest.updateRestaurant(restaurant);

    }

    @Test(expected = IllegalStateException.class)
    public void updateRestaurantShouldThrowIllegalStateExceptionWithNullPersistedInstance() {

        // Given
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(3L);

        Mockito.when(baseDao.get(Matchers.eq(Restaurant.class), Matchers.any(Long.class))).thenReturn(null);

        // When
        underTest.updateRestaurant(restaurant);

    }
}
