/*
 *
 */
package fr.midipascher.business;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
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
@ContextConfiguration(locations = { "classpath:META-INF/midipascher-domain.xml",
        "classpath:META-INF/midipascher-server.xml", "classpath:META-INF/midipascher-server-test.xml" })
public class FacadeImplTestIT {

    @Autowired
    private Facade facade;

    @Test
    public void createEntityShouldPersistAndSetId() throws Throwable {
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        // ensure id nullity
        foodSpecialty.setId(null);
        LoggerFactory.getLogger(FacadeImplTestIT.class).warn(
            "************************************** BEFORE CREATE FOOD SPECIALTY ******************************* ");
        // When
        final Long id = facade.createFoodSpecialty(foodSpecialty);

        LoggerFactory.getLogger(FacadeImplTestIT.class).warn(
            "************************************** AFTER CREATE FOOD SPECIALTY ******************************* ");
        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());

    }
}
