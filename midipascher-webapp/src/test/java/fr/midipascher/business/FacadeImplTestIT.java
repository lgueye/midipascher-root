/*
 *
 */
package fr.midipascher.business;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.test.TestUtils;

/**
 * Base class for database integration testing<br/>
 * Can not be instantiated<br/>
 * Does all the wiring plumbing<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/midipascher-server-test.xml" })
public class FacadeImplTestIT {

	@Autowired
	private Facade	facade;

	@Test
	public void createEntityShouldPersistAndSetId() throws Throwable {
		// Given
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		// ensure id nullity
		foodSpecialty.setId(null);
		// When
		final Long id = this.facade.createFoodSpecialty(foodSpecialty);

		// Then
		Assert.assertNotNull(id);
		Assert.assertEquals(id, foodSpecialty.getId());

	}

	@Test
	public void updateEntityShouldPersistProperties() throws Throwable {
		// Given
		FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		foodSpecialty.setId(null);
		// When
		final Long id = this.facade.createFoodSpecialty(foodSpecialty);
		// Then
		Assert.assertNotNull(id);
		Assert.assertEquals(id, foodSpecialty.getId());
		final String newCode = "New Code";
		final String newLabel = "Brand New Code";

		// Given
		foodSpecialty.setCode(newCode);
		foodSpecialty.setLabel(newLabel);

		// When
		this.facade.updateFoodSpecialty(foodSpecialty);

		foodSpecialty = this.facade.readFoodSpecialty(id);

		// Then
		Assert.assertEquals(newCode, foodSpecialty.getCode());
		Assert.assertEquals(newLabel, foodSpecialty.getLabel());

	}

	@Test
	public void deleteEntityShouldSucceed() throws Throwable {
		// Given
		FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		foodSpecialty.setId(null);
		// When
		final Long id = this.facade.createFoodSpecialty(foodSpecialty);
		// Then
		Assert.assertNotNull(id);
		Assert.assertEquals(id, foodSpecialty.getId());

		// When
		this.facade.deleteFoodSpecialty(foodSpecialty.getId());

		// Then
		Assert.assertNull(this.facade.readFoodSpecialty(id));
	}

}
