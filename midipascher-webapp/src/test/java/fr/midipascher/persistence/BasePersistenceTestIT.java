/*
 *
 */
package fr.midipascher.persistence;

import fr.midipascher.TestConstants;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
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

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Base class for database integration testing<br/>
 * Can not be instantiated<br/>
 * Does all the wiring plumbing<br/>
 *
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestConstants.SERVER_CONTEXT, TestConstants.VALIDATION_CONTEXT})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class BasePersistenceTestIT {

    @Autowired
    protected BaseDao baseDao;

    @Autowired
    protected DataSource dataSource;

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
