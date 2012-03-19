/*
 *
 */
package fr.midipascher.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.JpaConstants;
import fr.midipascher.persistence.RestaurantDao;

/**
 * @author louis.gueye@gmail.com
 */
@Repository(RestaurantDao.BEAN_ID)
public class RestaurantDaoImpl implements RestaurantDao {

	@PersistenceContext(unitName = JpaConstants.PERSISTANCE_UNIT_NAME)
	private EntityManager	entityManager;

	/**
	 * @see fr.midipascher.persistence.RestaurantDao#findByExample(fr.midipascher.domain.Restaurant)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Restaurant> findByExample(final Restaurant exampleInstance) {

		final Session session = (Session) entityManager.getDelegate();

		final Example example = Example.create(exampleInstance).excludeZeroes() // exclude
																				// zero
																				// valued
																				// properties
				.ignoreCase() // perform case insensitive string comparisons
				.enableLike(MatchMode.ANYWHERE); // use like for string
													// comparisons

		final Criteria restaurantCriteria = session.createCriteria(exampleInstance.getClass());

		restaurantCriteria.add(example);

		/**
		 * Matches row against collection elements<br/>
		 * If the row is associated with ANY of the provided collection's
		 * element it is included<br/>
		 * The match is based on and EXACT match of an element : all (except
		 * null one) provideded properties must match<br/>
		 * Example :<br/>
		 * Provided specialty = [id=null, code='CR', label='my label',
		 * active=true]<br/>
		 * Persisted specialty = [id=1, code='CR', label='my label',
		 * active=true] associated to Restaurant 1<br/>
		 * Persisted specialty = [id=2, code='CR', label='label', active=true]
		 * associated to Restaurant 2<br/>
		 * Persisted specialty = [id=3, code='CR', label='my label',
		 * active=false] associated to Restaurant 3<br/>
		 * Persisted specialty = [id=null, code=null, label='my label',
		 * active=true] associated to Restaurant 4<br/>
		 * Only restaurant 1 will match<br/>
		 */
		if (!CollectionUtils.isEmpty(exampleInstance.getSpecialties())) {

			restaurantCriteria.createAlias("specialties", "sp", CriteriaSpecification.INNER_JOIN);

			for ( final FoodSpecialty specialty : exampleInstance.getSpecialties() ) {

				if (specialty == null) {
					continue;
				}

				final String code = specialty.getCode();

				if (StringUtils.isNotEmpty(code)) {
					restaurantCriteria.add(Restrictions.eq("sp.code", code).ignoreCase());
				}

				final String label = specialty.getLabel();

				if (StringUtils.isNotEmpty(label)) {
					restaurantCriteria.add(Restrictions.eq("sp.label", label).ignoreCase());
				}

				final Long id = specialty.getId();

				if (id != null && id > 0) {
					restaurantCriteria.add(Restrictions.eq("sp.id", id));
				}

				final boolean active = specialty.isActive();

				restaurantCriteria.add(Restrictions.eq("sp.active", active));

			}

		}

		restaurantCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		return restaurantCriteria.list();

	}
}
