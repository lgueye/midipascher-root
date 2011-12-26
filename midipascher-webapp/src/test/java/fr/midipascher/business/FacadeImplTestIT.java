/*
 *
 */
package fr.midipascher.business;

import java.sql.Connection;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import fr.midipacher.TestConstants;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.User;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.test.TestUtils;

/**
 * Facade integration testing<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { TestConstants.SERVER_CONTEXT, TestConstants.SECURITY_CONTEXT })
public class FacadeImplTestIT {

	@Autowired
	private AuthenticationManager	authenticationManager;

	@Autowired
	private Facade					facade;

	@Autowired
	private DataSource				dataSource;

	@Before
	public void onSetUpInTransaction() throws Exception {
		final Connection con = DataSourceUtils.getConnection(this.dataSource);
		final IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
		final IDataSet dataSet = new FlatXmlDataSetBuilder().build(ResourceUtils
				.getFile(TestConstants.PERSISTENCE_TEST_DATA));

		try {
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
		} finally {
			DataSourceUtils.releaseConnection(con, this.dataSource);
		}
	}

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

	@Test
	public void createAccountShouldSucceed() {
		User user = TestUtils.validUser();
		Long id = this.facade.createAccount(user);
		User persistedUser = this.facade.readUser(id, true);
		Assert.assertNotNull(persistedUser);
		// When created associate by default with RMGR authority
		Assert.assertTrue(CollectionUtils.size(persistedUser.getAuthorities()) == 1);
		Assert.assertTrue(Authority.RMGR.equals(persistedUser.getAuthorities().iterator().next().getCode()));
	}

	@Test
	public void createFoodSpecialtyShouldSucceed() {
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("admin", "secret"));
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
	public void autorizationOrAuthenticationRequiredRequestShouldFailWhenNotAuthenticatedOrGrantedCorrectAuthority() {
		// Given
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		// ensure id nullity
		foodSpecialty.setId(null);
		try {
			// When
			final Long id = this.facade.createFoodSpecialty(foodSpecialty);
		} catch (AutenticationException e) {

		}
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("bob", "bob"));
		try {
			// When
			final Long id = this.facade.createFoodSpecialty(foodSpecialty);
		} catch (AuthorizationException e) {

		}

	}
}
