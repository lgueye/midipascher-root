/**
 * 
 */
package fr.midipascher.business;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.midipascher.business.impl.FacadeImpl;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Persistable;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.persistence.BaseDao;

/**
 * @author louis.gueye@gmail.com
 */
public class FacadeImplTest {

    @Mock
    private Validator validator;

    @Mock
    private BaseDao baseDao;

    @InjectMocks
    private final Facade underTest = new FacadeImpl();

    @Test
    public void createFoodSpecialtyShouldInvokePersistence() throws Throwable {

        // Given
        final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);

        Mockito.when(foodSpecialty.getId()).thenReturn(2L);

        // When
        underTest.createFoodSpecialty(foodSpecialty);

        // Then
        Mockito.verify(baseDao).persist(foodSpecialty);

        Mockito.verify(foodSpecialty, Mockito.times(2)).getId();

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
    public void createRestaurantShouldInvokePersistence() {

        // Given
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        // When
        underTest.createRestaurant(restaurant);

        // Then
        Mockito.verify(baseDao).persist(restaurant);

        Mockito.verify(restaurant).getId();

    }

    @Test(expected = IllegalArgumentException.class)
    public void createRestaurantShouldThrowIllegalArgumentExceptionWithNullRestaurant() {

        // Given
        final Restaurant restaurant = null;

        // When
        underTest.createRestaurant(restaurant);

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

        // When
        underTest.deleteRestaurant(restaurantId);

        // Then
        Mockito.verify(baseDao).delete(Restaurant.class, restaurantId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

        // Given
        final Long restaurantId = null;

        // When
        underTest.deleteRestaurant(restaurantId);

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
    public void getFoodSpecialtyShouldInvokePersistence() {

        // Given
        final Long foodSpecialtyId = 5L;

        // When
        underTest.readFoodSpecialty(foodSpecialtyId);

        // Then
        Mockito.verify(baseDao).get(FoodSpecialty.class, foodSpecialtyId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

        // Given
        final Long foodSpecialtyId = null;

        // When
        underTest.readFoodSpecialty(foodSpecialtyId);

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
    public void readRestaurantShouldInvokePersistence() {

        // Given
        final Long restaurantId = 5L;

        // When
        underTest.readRestaurant(restaurantId);

        // Then
        Mockito.verify(baseDao).get(Restaurant.class, restaurantId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void readRestaurantShouldThrowIllegalArgumentExceptionWithNullId() {

        // Given
        final Long restaurantId = null;

        // When
        underTest.readRestaurant(restaurantId);

    }

    @Test
    public void updateFoodSpecialtyShouldSetPropertiesThenInvokePersistence() {

        // Given
        final FoodSpecialty foodSpecialty = Mockito.mock(FoodSpecialty.class);

        final FoodSpecialty persistedInstance = Mockito.mock(FoodSpecialty.class);

        when(baseDao.get(eq(FoodSpecialty.class), anyLong())).thenReturn(persistedInstance);

        // When
        underTest.updateFoodSpecialty(foodSpecialty);

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
        underTest.updateFoodSpecialty(foodSpecialty);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFoodSpecialtyShouldThrowIllegalArgumentExceptionWithNullFoodSpecialtyId() {

        // Given
        final FoodSpecialty foodSpecialty = new FoodSpecialty();

        // When
        underTest.updateFoodSpecialty(foodSpecialty);

    }

    @Test(expected = IllegalStateException.class)
    public void updateFoodSpecialtyShouldThrowIllegalStateExceptionWithNullPersistedInstance() {

        // Given
        final FoodSpecialty foodSpecialty = new FoodSpecialty();

        foodSpecialty.setId(4L);

        when(baseDao.get(eq(FoodSpecialty.class), anyLong())).thenReturn(null);

        // When
        underTest.updateFoodSpecialty(foodSpecialty);

    }

    @Test
    public void updateRestaurantShouldSetPropertiesThenInvokePersistence() {

        // Given
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Restaurant persistedInstance = Mockito.mock(Restaurant.class);

        when(baseDao.get(eq(Restaurant.class), any(Long.class))).thenReturn(persistedInstance);

        // When
        underTest.updateRestaurant(restaurant);

        // Then
        Mockito.verify(persistedInstance).setAddress(restaurant.getAddress());

        Mockito.verify(persistedInstance).setDescription(restaurant.getDescription());

        Mockito.verify(persistedInstance).setEmail(restaurant.getEmail());

        Mockito.verify(persistedInstance).setKosher(restaurant.isKosher());

        Mockito.verify(persistedInstance).setHalal(restaurant.isHalal());

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

        when(baseDao.get(eq(Restaurant.class), any(Long.class))).thenReturn(null);

        // When
        underTest.updateRestaurant(restaurant);

    }

    @Test
    public void validateWillNotThrowExceptionWithEmptyViolationsSet() {
        // Given
        final Restaurant toBeValidated = new Restaurant();
        final ValidationContext validationContext = ValidationContext.CREATE;
        final Set<ConstraintViolation<Persistable>> violations = null;
        Mockito.when(validator.validate(Matchers.any(Persistable.class), Matchers.any(Class[].class))).thenReturn(
            violations);

        // When
        underTest.validate(toBeValidated, validationContext);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validateWillThrowExceptionWithNonEmptyViolationsSet() {
        // Given
        final Restaurant toBeValidated = new Restaurant();
        final ValidationContext validationContext = ValidationContext.CREATE;
        final Set<ConstraintViolation<Persistable>> violations = new HashSet<ConstraintViolation<Persistable>>();
        violations.add(new ConstraintViolationImpl<Persistable>("{message.template}", "interpolated message",
                Persistable.class, null, String.class, null, null, null, null));
        Mockito.when(validator.validate(Matchers.any(Persistable.class), Matchers.any(Class[].class))).thenReturn(
            violations);

        // When
        underTest.validate(toBeValidated, validationContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateWillThrowIllegalArgumentExceptionWithNullContext() {
        // Given
        final Restaurant toBeValidated = new Restaurant();
        final ValidationContext validationContext = null;

        // When
        underTest.validate(toBeValidated, validationContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateWillThrowIllegalArgumentExceptionWithNullObject() {
        // Given
        final Restaurant toBeValidated = null;
        final ValidationContext validationContext = ValidationContext.DELETE;

        // When
        underTest.validate(toBeValidated, validationContext);
    }
}
