/*
 *
 */
package fr.midipascher.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import fr.midipacher.TestConstants;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.User;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.test.TestUtils;

/**
 * Facade integration testing<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { TestConstants.SERVER_CONTEXT, TestConstants.SECURITY_CONTEXT,
		TestConstants.VALIDATION_CONTEXT })
public class FacadeImplTestIT {

	@Autowired
	private Facade		facade;

	@Autowired
	private DataSource	dataSource;

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

		SecurityContextHolder.setContext(new SecurityContextImpl());

	}

	@Test
	public void createFoodSpecialtyShouldSucceed() throws Throwable {
		authenticateAsAdmin();
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
	public void updateFoodSpecialtyShouldSucceed() throws Throwable {
		authenticateAsAdmin();
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
	public void inactivateFoodSpecialtyShouldSucceed() {
		authenticateAsAdmin();
		// Given
		FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		// ensure id nullity
		foodSpecialty.setId(null);
		// When
		final Long foodSpecialtyId = this.facade.createFoodSpecialty(foodSpecialty);
		foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		// Then
		Assert.assertNotNull(foodSpecialty);
		this.facade.inactivateFoodSpecialty(foodSpecialtyId);
		foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		Assert.assertFalse(foodSpecialty.isActive());
	}

	@Test
	public void deleteFoodSpecialtyShouldSucceed() throws Throwable {
		authenticateAsAdmin();
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
		authenticateAsAdmin();
		User user = TestUtils.validUser();
		Long id = this.facade.createAccount(user);
		User persistedUser = this.facade.readAccount(id, true);
		Assert.assertNotNull(persistedUser);
		// When created associate by default with RMGR authority
		Assert.assertTrue(CollectionUtils.size(persistedUser.getAuthorities()) == 1);
		Assert.assertTrue(Authority.RMGR.equals(persistedUser.getAuthorities().iterator().next().getCode()));
	}

	@Test
	public void updateAccountShouldSucceed() {
		authenticateAsAdmin();
		String email;
		User user;
		Long id;

		user = TestUtils.validUser();
		email = "first@email.org";
		user.setEmail(email);
		id = this.facade.createAccount(user);
		user = this.facade.readAccount(id);
		Assert.assertEquals(email, user.getEmail());

		email = "second@email.org";
		user.setEmail(email);
		this.facade.updateAccount(user);
		user = this.facade.readAccount(id);
		Assert.assertEquals(email, user.getEmail());
	}

	@Test
	public void persistingAccountWithNewRestaurantInCollectionShouldCreateRestaurant() {
		authenticateAsAdmin();
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		User user = TestUtils.validUser();
		Long userId = this.facade.createAccount(user);
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getSpecialties().clear();
		restaurant.getSpecialties().add(foodSpecialty);
		Long restaurantId = this.facade.createRestaurant(userId, restaurant);
		assertNotNull(restaurantId);
		restaurant = this.facade.readRestaurant(restaurantId, true);
		assertNotNull(restaurant);
		assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
		user = this.facade.readAccount(userId, true);
		assertNotNull(userId);
		assertEquals(1, CollectionUtils.size(user.getRestaurants()));
	}

	@Test
	public void deletingRestaurantShouldSucceed() {
		authenticateAsAdmin();

		// Given
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		User user = TestUtils.validUser();
		Long userId = this.facade.createAccount(user);
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getSpecialties().clear();
		restaurant.getSpecialties().add(foodSpecialty);
		Long restaurantId = this.facade.createRestaurant(userId, restaurant);
		assertNotNull(this.facade.readRestaurant(restaurantId));

		// When
		this.facade.deleteRestaurant(userId, restaurantId);

		// Then
		assertNull(this.facade.readRestaurant(restaurantId));

	}

	@Test
	public void deletingAccountShouldSucceed() {
		authenticateAsAdmin();

		// Given
		Long userId = this.facade.createAccount(TestUtils.validUser());
		assertNotNull(this.facade.readAccount(userId));
		// When
		this.facade.deleteAccount(userId);

		// Then
		assertNull(this.facade.readAccount(userId));

	}

	@Test
	public void deletingAccountShouldAlsoDeleteItsRestaurants() {
		authenticateAsAdmin();

		// Given
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		User user = TestUtils.validUser();
		Long userId = this.facade.createAccount(user);
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getSpecialties().clear();
		restaurant.getSpecialties().add(foodSpecialty);
		Long restaurantId = this.facade.createRestaurant(userId, restaurant);

		// When
		this.facade.deleteAccount(userId);

		// Then
		assertNull(this.facade.readAccount(userId));
		assertNull(this.facade.readRestaurant(restaurantId));
		assertNotNull(this.facade.readFoodSpecialty(foodSpecialtyId));

	}

	@Test
	public void updatingSimplePropertiesShouldSucceedWhenMergingRestaurant() {
		authenticateAsAdmin();

		// Given
		Long restaurantId = createRestaurant();
		assertNotNull(restaurantId);
		Restaurant restaurant = this.facade.readRestaurant(restaurantId);

		String city = "new city";
		String countryCode = "fr";
		String postalCode = "92800";
		String streetAddress = "new street address";
		String companyId = "new company id";
		String description = "new description";
		boolean halal = true;
		boolean kosher = false;
		String mainOffer = "new main offer";
		String name = "new name";
		String phoneNumber = "new phone number";
		boolean vegetarian = true;

		restaurant.getAddress().setCity(city);
		restaurant.getAddress().setCountryCode(countryCode);
		restaurant.getAddress().setPostalCode(postalCode);
		restaurant.getAddress().setStreetAddress(streetAddress);
		restaurant.setCompanyId(companyId);
		restaurant.setDescription(description);
		restaurant.setHalal(halal);
		restaurant.setKosher(kosher);
		restaurant.setMainOffer(mainOffer);
		restaurant.setName(name);
		restaurant.setPhoneNumber(phoneNumber);
		restaurant.setVegetarian(vegetarian);
		// When
		this.facade.updateRestaurant(restaurant);
		restaurant = this.facade.readRestaurant(restaurantId);

		// Then
		assertEquals(city, restaurant.getAddress().getCity());
		assertEquals(countryCode, restaurant.getAddress().getCountryCode());
		assertEquals(postalCode, restaurant.getAddress().getPostalCode());
		assertEquals(streetAddress, restaurant.getAddress().getStreetAddress());
		assertEquals(companyId, restaurant.getCompanyId());
		assertEquals(description, restaurant.getDescription());
		assertEquals(mainOffer, restaurant.getMainOffer());
		assertEquals(name, restaurant.getName());
		assertEquals(phoneNumber, restaurant.getPhoneNumber());
	}

	/**
	 * @return
	 */
	private Long createRestaurant() {
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		User user = TestUtils.validUser();
		Long userId = this.facade.createAccount(user);
		Restaurant restaurant = TestUtils.validRestaurant();
		restaurant.getSpecialties().clear();
		restaurant.getSpecialties().add(foodSpecialty);
		Long restaurantId = this.facade.createRestaurant(userId, restaurant);
		return restaurantId;
	}

	@Test
	public void addingSpecialtyShouldSucceedWhenMergingRestaurant() {
		authenticateAsAdmin();

		// Given
		Long restaurantId = createRestaurant();
		assertNotNull(restaurantId);
		Restaurant restaurant = this.facade.readRestaurant(restaurantId, true);
		assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		restaurant.addSpecialty(foodSpecialty);

		// When
		this.facade.updateRestaurant(restaurant);
		restaurant = this.facade.readRestaurant(restaurantId, true);

		// Then
		assertEquals(2, CollectionUtils.size(restaurant.getSpecialties()));
	}

	@Test
	public void removingSpecialtyShouldSucceedWhenMergingRestaurant() {
		authenticateAsAdmin();

		// Given
		Long restaurantId = createRestaurant();
		assertNotNull(restaurantId);
		Restaurant restaurant = this.facade.readRestaurant(restaurantId, true);
		assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		restaurant.addSpecialty(foodSpecialty);
		this.facade.updateRestaurant(restaurant);
		restaurant = this.facade.readRestaurant(restaurantId, true);
		assertEquals(2, CollectionUtils.size(restaurant.getSpecialties()));
		restaurant.getSpecialties().remove(foodSpecialty);

		// When
		this.facade.updateRestaurant(restaurant);
		restaurant = this.facade.readRestaurant(restaurantId, true);

		// Then
		assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
	}

	@Test
	public void updatingRestaurantSpecialtyShouldNotSucceedWhenMerging() {
		authenticateAsAdmin();

		// Given
		Long restaurantId = createRestaurant();
		assertNotNull(restaurantId);
		Restaurant restaurant = this.facade.readRestaurant(restaurantId, true);
		assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
		Long foodSpecialtyId = this.facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
		FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
		assertNotNull(foodSpecialty);
		restaurant.addSpecialty(foodSpecialty);

		// When
		this.facade.updateRestaurant(restaurant);
		restaurant = this.facade.readRestaurant(restaurantId, true);

		assertEquals(2, CollectionUtils.size(restaurant.getSpecialties()));
		String newLabel = "new Label";
		String label = restaurant.getSpecialties().iterator().next().getLabel();
		foodSpecialty = restaurant.getSpecialties().iterator().next();
		foodSpecialty.setLabel(newLabel);

		// When
		this.facade.updateRestaurant(restaurant);
		restaurant = this.facade.readRestaurant(restaurantId, true);

		for ( FoodSpecialty specialty : restaurant.getSpecialties() )
			if (specialty.equals(foodSpecialty)) assertEquals(label, specialty.getLabel());

	}

	@Test
	public void persistingAccountWithRemovedRestaurantFromCollectionShouldDeleteRestaurant() {
	}

	@Test
	public void autorizationOrAuthenticationRequiredRequestShouldFailWhenNotAuthenticatedOrGrantedCorrectAuthority() {
		// Given
		final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
		// ensure id nullity
		foodSpecialty.setId(null);
		try {
			// When
			this.facade.createFoodSpecialty(foodSpecialty);
			Assert.fail(AuthenticationException.class.getSimpleName() + " expected");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Throwable th) {
			Assert.fail(AuthenticationException.class.getSimpleName() + " expected,  got = " + th);
		}
		authenticateAsRmgr();
		try {
			// When
			this.facade.createFoodSpecialty(foodSpecialty);
			Assert.fail(AuthenticationException.class.getSimpleName() + " expected");
		} catch (AccessDeniedException e) {
		} catch (Throwable th) {
			Assert.fail(AuthenticationException.class.getSimpleName() + " expected,  got = " + th);
		}

	}

	private void authenticateAs(String uid, String password, Collection<? extends GrantedAuthority> authorities) {
		SecurityContext securityContext = new SecurityContextImpl();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(uid, password,
				authorities);
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	private void authenticateAsAdmin() {
		authenticateAs("admin", "secret", Arrays.asList(new GrantedAuthorityImpl("ROLE_ADMIN")));
	}

	private void authenticateAsRmgr() {
		authenticateAs("rmgr", "secret", Arrays.asList(new GrantedAuthorityImpl("ROLE_RMGR")));
	}

}
