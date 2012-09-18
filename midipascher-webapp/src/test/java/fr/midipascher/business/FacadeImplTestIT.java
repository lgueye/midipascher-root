/*
 *
 */
package fr.midipascher.business;

import fr.midipascher.TestConstants;
import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.business.Facade;
import fr.midipascher.domain.exceptions.BusinessException;
import fr.midipascher.test.TestFixtures;
import junit.framework.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Facade integration testing<br/>
 *
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestConstants.SERVER_CONTEXT, TestConstants.SECURITY_CONTEXT,
        TestConstants.VALIDATION_CONTEXT, TestConstants.SEARCH_CONTEXT})
public class FacadeImplTestIT {

    @Autowired
    private Facade facade;

    @Autowired
    private DataSource dataSource;

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
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        // ensure id nullity
        foodSpecialty.setId(null);
        // When
        final Long id = this.facade.createFoodSpecialty(foodSpecialty);

        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());
        Assert.assertNotNull(this.facade.readFoodSpecialty(id));

    }

    @Test
    public void updateFoodSpecialtyShouldSucceed() throws Throwable {
        authenticateAsAdmin();
        // Given
        FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
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
        this.facade.updateFoodSpecialty(id, foodSpecialty);

        foodSpecialty = this.facade.readFoodSpecialty(id);

        // Then
        Assert.assertEquals(newCode, foodSpecialty.getCode());
        Assert.assertEquals(newLabel, foodSpecialty.getLabel());

    }

    @Test
    public void inactivateFoodSpecialtyShouldSucceed() {
        authenticateAsAdmin();
        // Given
        FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
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
        FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
        foodSpecialty.setId(null);
        // When
        final Long id = this.facade.createFoodSpecialty(foodSpecialty);
        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());

        // When
        this.facade.deleteFoodSpecialty(foodSpecialty.getId());

        // Then
        try {
            this.facade.readFoodSpecialty(id);
            fail(BusinessException.class.getName() + " expected");
        } catch (BusinessException e) {
            Assert.assertEquals("foodSpecialty.not.found", e.getMessageCode());
        } catch (Throwable th) {
            fail(BusinessException.class.getName() + " expected, got " + th.getClass().getName());
        }
    }

    @Test
    public void createAccountShouldSucceed() {
        authenticateAsAdmin();
        Account account = TestFixtures.validAccount();
        Long id = this.facade.createAccount(account);
        Account persistedUser = this.facade.readAccount(id, true);
        Assert.assertNotNull(persistedUser);
        // When created associate by default with RMGR authority
        Assert.assertTrue(CollectionUtils.size(persistedUser.getAuthorities()) == 1);
        Assert.assertTrue(Authority.RMGR.equals(persistedUser.getAuthorities().iterator().next().getCode()));
    }

    @Test
    public void updateAccountShouldSucceed() {
        authenticateAsAdmin();
        String email;
        Account account;
        Long id;

        account = TestFixtures.validAccount();
        email = "first@email.org";
        account.setEmail(email);
        id = this.facade.createAccount(account);
        account = this.facade.readAccount(id, false);
        Assert.assertEquals(email, account.getEmail());

        email = "second@email.org";
        account.setEmail(email);
        this.facade.updateAccount(id, account);
        account = this.facade.readAccount(id, false);
        Assert.assertEquals(email, account.getEmail());
    }

    @Test
    public void persistingAccountWithNewRestaurantInCollectionShouldCreateRestaurant() {
        authenticateAsAdmin();
        Long foodSpecialtyId = this.facade.createFoodSpecialty(TestFixtures.validFoodSpecialty());
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getSpecialties().clear();
        restaurant.getSpecialties().add(foodSpecialty);
        Long restaurantId = this.facade.createRestaurant(accountId, restaurant);
        assertNotNull(restaurantId);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, true);
        assertNotNull(restaurant);
        assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
        account = this.facade.readAccount(accountId, true);
        assertNotNull(accountId);
        assertEquals(1, CollectionUtils.size(account.getRestaurants()));
    }

    @Test
    public void deletingRestaurantShouldSucceed() {
        authenticateAsAdmin();

        // Given
        Long foodSpecialtyId = 1L;
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getSpecialties().clear();
        restaurant.getSpecialties().add(foodSpecialty);
        Long restaurantId = this.facade.createRestaurant(accountId, restaurant);
        assertNotNull(this.facade.readRestaurant(accountId, restaurantId, false));

        // When
        this.facade.deleteRestaurant(accountId, restaurantId);

        // Then
        try {
            this.facade.readRestaurant(accountId, restaurantId, false);
            fail(BusinessException.class.getName() + " expected");
        } catch (BusinessException e) {
            Assert.assertEquals("restaurant.not.found", e.getMessageCode());
        } catch (Throwable th) {
            th.printStackTrace();
            fail(BusinessException.class.getName() + " expected, got " + th.getClass().getName());
        }

    }

    @Test
    public void deletingAccountShouldSucceed() {
        authenticateAsAdmin();

        // Given
        Long accountId = this.facade.createAccount(TestFixtures.validAccount());
        assertNotNull(this.facade.readAccount(accountId, false));

        // When
        this.facade.deleteAccount(accountId);

        // Then
        try {
            this.facade.readAccount(accountId, false);
            fail(BusinessException.class.getName() + " expected");
        } catch (BusinessException e) {
            Assert.assertEquals("account.not.found", e.getMessageCode());
        } catch (Throwable th) {
            fail(BusinessException.class.getName() + " expected, got " + th.getClass().getName());
        }

    }

    @Test
    @Ignore
    public void deletingAccountShouldAlsoDeleteItsRestaurants() {
        authenticateAsAdmin();

        // Given
        Long foodSpecialtyId = 1L;
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.getSpecialties().clear();
        restaurant.getSpecialties().add(foodSpecialty);
        Long restaurantId = this.facade.createRestaurant(accountId, restaurant);

        // When
        this.facade.deleteAccount(accountId);

        // Then
        assertNull(this.facade.readAccount(accountId, false));

        assertNull(this.facade.readRestaurant(accountId, restaurantId, false));
        assertNotNull(this.facade.readFoodSpecialty(foodSpecialtyId));

    }

    @Test
    public void updatingSimplePropertiesShouldSucceedWhenMergingRestaurant() throws Throwable {
        authenticateAsAdmin();

        // Given
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Long restaurantId = createRestaurant(accountId);
        assertNotNull(restaurantId);
        Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

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
        this.facade.updateRestaurant(accountId, restaurantId, restaurant);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, false);

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
     * @throws Throwable
     */
    private Long createRestaurant(Long accountId) throws Throwable {
        Long foodSpecialtyId = this.facade.createFoodSpecialty(TestFixtures.validFoodSpecialty());
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        Restaurant restaurant = TestFixtures.validRestaurant();
        restaurant.clearSpecialties();
        restaurant.addSpecialty(foodSpecialty);
        try {
            Long restaurantId = this.facade.createRestaurant(accountId, restaurant);
            return restaurantId;
        } catch (ConstraintViolationException th) {
            System.out
                    .println("----------------------------------------------------------------------------------------------"
                            + th.getConstraintViolations().iterator().next().getMessage());
            throw th;
        }
    }

    @Test
    public void addingSpecialtyShouldSucceedWhenMergingRestaurant() throws Throwable {
        authenticateAsAdmin();

        // Given
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Long restaurantId = createRestaurant(accountId);
        assertNotNull(restaurantId);
        Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);
        assertEquals(1, restaurant.countSpecialties());
        Long foodSpecialtyId = this.facade.createFoodSpecialty(TestFixtures.validFoodSpecialty());
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        restaurant.addSpecialty(foodSpecialty);

        // When
        this.facade.updateRestaurant(accountId, restaurantId, restaurant);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

        // Then
        assertEquals(2, restaurant.countSpecialties());
    }

    @Test
    public void removingSpecialtyShouldSucceedWhenMergingRestaurant() throws Throwable {
        authenticateAsAdmin();

        // Given
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Long restaurantId = createRestaurant(accountId);
        assertNotNull(restaurantId);
        Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

        assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
        Long foodSpecialtyId = this.facade.createFoodSpecialty(TestFixtures.validFoodSpecialty());
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        restaurant.addSpecialty(foodSpecialty);
        this.facade.updateRestaurant(accountId, restaurantId, restaurant);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, true);
        assertEquals(2, CollectionUtils.size(restaurant.getSpecialties()));
        restaurant.getSpecialties().remove(foodSpecialty);

        // When
        this.facade.updateRestaurant(accountId, restaurantId, restaurant);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

        // Then
        assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
    }

    @Test
    public void updatingRestaurantSpecialtyShouldNotSucceedWhenMerging() throws Throwable {
        authenticateAsAdmin();

        // Given
        Account account = TestFixtures.validAccount();
        Long accountId = this.facade.createAccount(account);
        Long restaurantId = createRestaurant(accountId);
        assertNotNull(restaurantId);
        Restaurant restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

        assertEquals(1, CollectionUtils.size(restaurant.getSpecialties()));
        Long foodSpecialtyId = this.facade.createFoodSpecialty(TestFixtures.validFoodSpecialty());
        FoodSpecialty foodSpecialty = this.facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        restaurant.addSpecialty(foodSpecialty);

        // When
        this.facade.updateRestaurant(accountId, restaurantId, restaurant);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

        assertEquals(2, CollectionUtils.size(restaurant.getSpecialties()));
        String newLabel = "new Label";
        String label = restaurant.getSpecialties().iterator().next().getLabel();
        foodSpecialty = restaurant.getSpecialties().iterator().next();
        foodSpecialty.setLabel(newLabel);

        // When
        this.facade.updateRestaurant(accountId, restaurantId, restaurant);
        restaurant = this.facade.readRestaurant(accountId, restaurantId, true);

        for (FoodSpecialty specialty : restaurant.getSpecialties())
            if (specialty.equals(foodSpecialty))
                assertEquals(label, specialty.getLabel());

    }

    @Test
    public void persistingAccountWithRemovedRestaurantFromCollectionShouldDeleteRestaurant() {
    }

    @Test
    public void autorizationOrAuthenticationRequiredRequestShouldFailWhenNotAuthenticatedOrGrantedCorrectAuthority() {
        // Given
        final FoodSpecialty foodSpecialty = TestFixtures.validFoodSpecialty();
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
        authenticateAs("admin@admin.com", "secret", Arrays.asList(new SimpleGrantedAuthority(Authority.ROLE_ADMIN)));
    }

    private void authenticateAsRmgr() {
        authenticateAs("rmgr@rmgr.com", "secret", Arrays.asList(new SimpleGrantedAuthority(Authority.ROLE_RMGR)));
    }

    @Test
    public void createRestaurantShouldThrowNotFoundExceptionWithUnknownUser() {
        authenticateAsRmgr();

        Long accountId;
        Restaurant restaurant;

        accountId = -1L;
        restaurant = TestFixtures.validRestaurant();
        LocaleContextHolder.setLocale(Locale.FRENCH);

        try {
            this.facade.createRestaurant(accountId, restaurant);
            fail(BusinessException.class.getSimpleName() + " expected");
        } catch (BusinessException e) {
            assertEquals("account.not.found", e.getMessageCode());
        } catch (Throwable th) {
            fail(BusinessException.class.getSimpleName() + " expected, got = " + th);
        }

    }

    @Test
    public void findRestaurantByByNameShouldSucceed() throws Throwable {
        final Long id = 8L;
        int expectedHitsCount;
        List<Restaurant> actualResponse;
        Restaurant restaurant;
        String name;
        String query;
        Restaurant criteria;

        // Given I index that data
        name = "Gouts et saveurs";
        restaurant = TestFixtures.validRestaurant();
        restaurant.setName(name);
        authenticateAsRmgr();
        Long accountId = facade.createAccount(TestFixtures.validAccount());
        facade.createRestaurant(accountId, restaurant);
        createRestaurant(accountId);
        expectedHitsCount = 1;

        // When I search
        query = "gouts";
        criteria = new Restaurant();
        criteria.setName(name);
        actualResponse = facade.findRestaurantsByCriteria(criteria);
        // Then I should get 1 hit
        assertEquals(expectedHitsCount, actualResponse.size());

        // When I search
        query = "goûts";
        criteria = new Restaurant();
        criteria.setName(name);
        actualResponse = facade.findRestaurantsByCriteria(criteria);
        // Then I should get 1 hit
        assertEquals(expectedHitsCount, actualResponse.size());

        // When I search
        query = "saveurs";
        criteria = new Restaurant();
        criteria.setName(name);
        actualResponse = facade.findRestaurantsByCriteria(criteria);
        // Then I should get 1 hit
        assertEquals(expectedHitsCount, actualResponse.size());
    }

}
