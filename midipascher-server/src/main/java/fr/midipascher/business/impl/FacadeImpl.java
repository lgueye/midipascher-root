/*
 *
 */
package fr.midipascher.business.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.persistence.BaseDao;

/**
 * @author louis.gueye@gmail.com
 */
@Service(Facade.BEAN_ID)
public class FacadeImpl implements Facade {

    @Autowired
    private Validator validator;

    @Autowired
    private BaseDao baseDao;

    /**
     * @see fr.midipascher.domain.business.Facade#createFoodSpecialty(fr.midipascher.domain.FoodSpecialty)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long createFoodSpecialty(final FoodSpecialty foodSpecialty) {

        Preconditions.checkArgument(foodSpecialty != null,
            "Illegal call to createFoodSpecialty, foodSpecialty is required");

        baseDao.persist(foodSpecialty);

        Preconditions.checkState(foodSpecialty.getId() != null, "foodSpecialty id should not be null");

        return foodSpecialty.getId();

    }

    /**
     * @see fr.midipascher.domain.business.Facade#createRestaurant(fr.midipascher.domain.Restaurant)
     */
    @Override
    public Long createRestaurant(final Restaurant restaurant) {

        Preconditions.checkArgument(restaurant != null, "Illegal call to createRestaurant, restaurant is required");

        baseDao.persist(restaurant);

        return restaurant.getId();

    }

    /**
     * @see fr.midipascher.domain.business.Facade#deleteFoodSpecialty(java.lang.Long)
     */
    @Override
    public void deleteFoodSpecialty(final Long foodSpecialtyId) {

        Preconditions.checkArgument(foodSpecialtyId != null,
            "Illegal call to deleteFoodSpecialty, foodSpecialtyId is required");

        baseDao.delete(FoodSpecialty.class, foodSpecialtyId);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#deleteRestaurant(java.lang.Long)
     */
    @Override
    public void deleteRestaurant(final Long restaurantId) {

        Preconditions.checkArgument(restaurantId != null,
            "Illegal call to deleteRestaurant, restaurant identifier is required");

        baseDao.delete(Restaurant.class, restaurantId);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#findRestaurantsByCriteria(fr.midipascher.domain.Restaurant)
     */
    @Override
    public List<Restaurant> findRestaurantsByCriteria(final Restaurant criteria) {

        Preconditions
                .checkArgument(criteria != null, "Illegal call to findRestaurantsByCriteria, criteria is required");

        return baseDao.findByExample(criteria);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#listFoodSpecialties()
     */
    @Override
    public List<FoodSpecialty> listFoodSpecialties() {

        return baseDao.findAll(FoodSpecialty.class);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#readFoodSpecialty(java.lang.Long)
     */
    @Override
    public FoodSpecialty readFoodSpecialty(final Long foodSpecialtyId) {

        Preconditions.checkArgument(foodSpecialtyId != null,
            "Illegal call to readFoodSpecialty, foodSpecialtyId is required");

        return baseDao.get(FoodSpecialty.class, foodSpecialtyId);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#readRestaurant(java.lang.Long)
     */
    @Override
    public Restaurant readRestaurant(final Long restaurantId) {

        Preconditions.checkArgument(restaurantId != null,
            "Illegal call to readRestaurant, restaurant identifier is required");

        return baseDao.get(Restaurant.class, restaurantId);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#updateFoodSpecialty(fr.midipascher.domain.FoodSpecialty)
     */
    @Override
    public void updateFoodSpecialty(final FoodSpecialty foodSpecialty) {

        Preconditions.checkArgument(foodSpecialty != null,
            "Illegal call to updateFoodSpecialty, foodSpecialty is required");

        baseDao.persist(foodSpecialty);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#updateRestaurant(fr.midipascher.domain.Restaurant)
     */
    @Override
    public void updateRestaurant(final Restaurant restaurant) {

        Preconditions.checkArgument(restaurant != null, "Illegal call to updateRestaurant, restaurant is required");

        baseDao.persist(restaurant);

    }

    /**
     * @see fr.midipascher.domain.business.Facade#validate(fr.midipascher.domain.AbstractEntity,
     *      fr.midipascher.domain.validation.ValidationContext)
     */
    @Override
    public <T extends AbstractEntity> void validate(final T type, final ValidationContext context) {

        Preconditions.checkArgument(type != null, "Illegal call to validate, object is required");

        Preconditions.checkArgument(context != null, "Illegal call to validate, validation context is required");

        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(type, context.getContext());

        if (CollectionUtils.isEmpty(constraintViolations))
            return;

        final Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
                constraintViolations.size());

        final Set<String> classNames = new HashSet<String>();

        for (final ConstraintViolation<?> violation : constraintViolations) {

            propagatedViolations.add(violation);

            classNames.add(violation.getLeafBean().getClass().getName());

        }

        throw new ConstraintViolationException(propagatedViolations);

    }

}
