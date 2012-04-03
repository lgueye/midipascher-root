/*
 *
 */
package fr.midipascher.persistence;

import java.sql.Connection;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import fr.midipacher.TestConstants;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Persistable;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestUtils;

/**
 * Base class for database integration testing<br/>
 * Can not be instantiated<br/>
 * Does all the wiring plumbing<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { TestConstants.SERVER_CONTEXT, TestConstants.VALIDATION_CONTEXT })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class BasePersistenceTestIT {

    @Autowired
    protected BaseDao baseDao;

    @Autowired
    protected DataSource dataSource;

    /**
     * @param underTest
     * @param context
     */
    protected void assertExpectedViolation(final Persistable underTest, final ValidationContext context,
            final String errorCode, final String errorPath) {
        // When
        try {

            switch (context) {
                case CREATE:
                case UPDATE:
                    baseDao.persist(underTest);
                    break;
                case DELETE:
                    baseDao.delete(underTest.getClass(), underTest.getId());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported validation context : " + context);
            }

            Assert.fail(ConstraintViolationException.class.getName() + " expected");

            // Then
        } catch (final ConstraintViolationException e) {
            TestUtils.assertViolationContainsTemplateAndPath(e, errorCode, errorPath);
        } catch (final Throwable th) {
            th.printStackTrace();
            Assert.fail(ConstraintViolationException.class.getName() + " expected, got class="
                + th.getClass().getName() + ", message=" + th.getLocalizedMessage() + ", cause=" + th.getCause());
        }
    }

    @Before
    public void onSetUpInTransaction() throws Exception {
        final Connection con = DataSourceUtils.getConnection(dataSource);
        final IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
        final IDataSet dataSet = new FlatXmlDataSetBuilder().build(ResourceUtils
                .getFile(TestConstants.PERSISTENCE_TEST_DATA));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
        Assert.assertEquals(2, baseDao.findAll(Authority.class).size());
        Assert.assertEquals(5, baseDao.findAll(FoodSpecialty.class).size());
        Assert.assertEquals(2, baseDao.findAll(Restaurant.class).size());
    }

}
