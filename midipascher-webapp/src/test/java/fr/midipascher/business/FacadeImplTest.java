/**
 * 
 */
package fr.midipascher.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.midipascher.business.impl.FacadeImpl;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Persistable;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.User;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.persistence.BaseDao;

/**
 * @author louis.gueye@gmail.com
 */
public class FacadeImplTest {

	@Mock
	private Validator		validator;

	@Mock
	private BaseDao			baseDao;

	@InjectMocks
	private final Facade	underTest	= new FacadeImpl();

	@Test
	public void createFoodSpecialtyShouldInvokePersistence() throws Throwable {

		// Given
		final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);

		Mockito.when(foodSpecialty.getId()).thenReturn(2L);

		// When
		this.underTest.createFoodSpecialty(foodSpecialty);

		// Then
		Mockito.verify(this.baseDao).persist(foodSpecialty);

		Mockito.verify(foodSpecialty, Mockito.times(2)).getId();

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

	@Test(expected = IllegalArgumentException.class)
	public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {

		// Given
		final Restaurant restaurant = null;

		// When
		this.underTest.createRestaurant(restaurant);

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

		// When
		this.underTest.deleteRestaurant(restaurantId);

		// Then
		Mockito.verify(this.baseDao).delete(Restaurant.class, restaurantId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long restaurantId = null;

		// When
		this.underTest.deleteRestaurant(restaurantId);

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

		// Given
		final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);

		final FoodSpecialty persistedInstance = Mockito.mock(FoodSpecialty.class);

		Mockito.when(this.baseDao.get(Matchers.eq(FoodSpecialty.class), Matchers.anyLong())).thenReturn(
				persistedInstance);

		// When
		this.underTest.updateFoodSpecialty(foodSpecialty);

		// Then
		Mockito.verify(persistedInstance).setActive(foodSpecialty.isActive());

		Mockito.verify(persistedInstance).setCode(foodSpecialty.getCode());

		Mockito.verify(persistedInstance).setLabel(foodSpecialty.getLabel());

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
		this.underTest.readUser(id);
	}

	public void readUserShouldInvokeBaseDao() {
		// Given
		Long id = 5L;
		User expectedUser = Mockito.mock(User.class);
		// When
		Mockito.when(this.baseDao.get(User.class, id)).thenReturn(expectedUser);
		User actualUser = this.underTest.readUser(id);

		Mockito.verify(this.baseDao).get(User.class, id);
		Assert.assertSame(expectedUser, actualUser);
		Mockito.verifyNoMoreInteractions(this.baseDao);
	}

}
