/*
 *
 */
package fr.midipascher.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.ResourceUtils;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.test.TestUtils;

/**
 * FoodSpecialty database integration testing<br/>
 * CRUD operations are tested<br>
 * Finders are tested<br/>
 * 
 * @author louis.gueye@gmail.com
 */
public class FoodSpecialtyPersistenceTestIT extends BasePersistenceTestIT {

    /**
     * @param longs
     */
    private void assertResultContainsFoodSpecialtyIds(final List<FoodSpecialty> result, final Set<Long> ids) {
        if (CollectionUtils.isEmpty(result) && ids == null)
            return;

        final Set<Long> foodSpecialtyIds = new HashSet<Long>();
        for (final FoodSpecialty foodSpecialty : result) {
            foodSpecialtyIds.add(foodSpecialty.getId());
        }

        assertTrue(foodSpecialtyIds.containsAll(ids));
    }

    @Before
    public void onSetUpInTransaction() throws Exception {
        final Connection con = DataSourceUtils.getConnection(dataSource);
        final IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
        final IDataSet dataSet = new FlatXmlDataSetBuilder().build(ResourceUtils
                .getFile("classpath:dbunit/midipascher-test-data.xml"));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
        assertEquals(5, baseDao.findAll(FoodSpecialty.class).size());
    }

    /**
     * Given : a valid food specialty<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should retrieve it in database<br/>
     */
    @Test
    public void shouldCreateFoodSpecialty() {
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();

        // When
        baseDao.persist(foodSpecialty);
        baseDao.flush();

        // Then
        assertNotNull(foodSpecialty.getId());
        assertEquals(foodSpecialty, baseDao.get(FoodSpecialty.class, foodSpecialty.getId()));
    }

    /**
     * Given : a valid foodSpecialty<br/>
     * When : one persists the above food specialty and then delete it<br/>
     * Then : system should not retrieve it in database<br/>
     */
    @Test
    public void shouldDeleteFoodSpecialty() {
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();

        // When
        baseDao.persist(foodSpecialty);
        baseDao.flush();
        baseDao.delete(FoodSpecialty.class, foodSpecialty.getId());
        baseDao.flush();

        // Then
        final FoodSpecialty persistedFoodSpecialty = baseDao.get(FoodSpecialty.class, foodSpecialty.getId());
        assertNull(persistedFoodSpecialty);
    }

    /**
     * Given : one food specialty with label property valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return food specialty {id = 2}<br/>
     */
    @Test
    public void shouldFindFoodSpecialtyByLabel() {
        // Given
        final FoodSpecialty foodSpecialty = new FoodSpecialty();
        foodSpecialty.setLabel("ang");

        // When
        final List<FoodSpecialty> results = baseDao.findByExample(foodSpecialty);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertResultContainsFoodSpecialtyIds(results, new HashSet<Long>(Arrays.asList(3L)));

    }

    /**
     * Given : a valid food specialty<br/>
     * When : one updates that food specialty<br/>
     * Then : system should persist changes<br/>
     */
    @Test
    public void shouldUpdateFoodSpecialty() {
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        baseDao.persist(foodSpecialty);
        baseDao.flush();
        baseDao.evict(foodSpecialty);
        final String label = RandomStringUtils.random(50);
        foodSpecialty.setLabel(label);

        // When
        baseDao.merge(foodSpecialty);
        baseDao.flush();
        final FoodSpecialty persistedFoodSpecialty = baseDao.get(FoodSpecialty.class, foodSpecialty.getId());

        // Then
        assertEquals(foodSpecialty.getLabel(), persistedFoodSpecialty.getLabel());
    }

}
