/*
 *
 */
package fr.midipascher.business;

import static org.junit.Assert.assertNotNull;

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
@ContextConfiguration(locations = { TestConstants.SERVER_CONTEXT, TestConstants.SECURITY_CONTEXT })
public class FacadeImplTestIT {

    @Autowired
    private Facade facade;

    @Autowired
    private DataSource dataSource;

    private void authenticateAs(final String uid, final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        final SecurityContext securityContext = new SecurityContextImpl();
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(uid,
                password, authorities);
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void authenticateAsAdmin() {
        authenticateAs("admin", "secret", Arrays.asList(new GrantedAuthorityImpl("ROLE_ADMIN")));
    }

    private void authenticateAsRmgr() {
        authenticateAs("bob", "bob", Arrays.asList(new GrantedAuthorityImpl("ROLE_USER")));
    }

    @Test
    public void autorizationOrAuthenticationRequiredRequestShouldFailWhenNotAuthenticatedOrGrantedCorrectAuthority() {
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        // ensure id nullity
        foodSpecialty.setId(null);
        try {
            // When
            facade.createFoodSpecialty(foodSpecialty);
            Assert.fail(AuthenticationException.class.getSimpleName() + " expected");
        } catch (final AuthenticationCredentialsNotFoundException e) {} catch (final Throwable th) {
            Assert.fail(AuthenticationException.class.getSimpleName() + " expected,  got = " + th);
        }
        authenticateAsRmgr();
        try {
            // When
            facade.createFoodSpecialty(foodSpecialty);
            Assert.fail(AuthenticationException.class.getSimpleName() + " expected");
        } catch (final AccessDeniedException e) {} catch (final Throwable th) {
            Assert.fail(AuthenticationException.class.getSimpleName() + " expected,  got = " + th);
        }

    }

    @Test
    public void createAccountShouldSucceed() {
        authenticateAsAdmin();
        final User user = TestUtils.validUser();
        final Long id = facade.createAccount(user);
        final User persistedUser = facade.readUser(id, true);
        Assert.assertNotNull(persistedUser);
        // When created associate by default with RMGR authority
        Assert.assertTrue(CollectionUtils.size(persistedUser.getAuthorities()) == 1);
        Assert.assertTrue(Authority.RMGR.equals(persistedUser.getAuthorities().iterator().next().getCode()));
    }

    @Test
    public void createEntityShouldPersistAndSetId() throws Throwable {
        authenticateAsAdmin();
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        // ensure id nullity
        foodSpecialty.setId(null);
        // When
        final Long id = facade.createFoodSpecialty(foodSpecialty);

        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());

    }

    @Test
    public void createFoodSpecialtyShouldSucceed() {
        authenticateAsAdmin();
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        // ensure id nullity
        foodSpecialty.setId(null);
        // When
        final Long id = facade.createFoodSpecialty(foodSpecialty);

        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());
    }

    @Test
    public void deleteEntityShouldSucceed() throws Throwable {
        authenticateAsAdmin();
        // Given
        final FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        foodSpecialty.setId(null);
        // When
        final Long id = facade.createFoodSpecialty(foodSpecialty);
        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());

        // When
        facade.deleteFoodSpecialty(foodSpecialty.getId());

        // Then
        Assert.assertNull(facade.readFoodSpecialty(id));
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

        SecurityContextHolder.setContext(new SecurityContextImpl());

    }

    @Test
    public void persistingAccountWithNewRestaurantInCollectionShouldCreateRestaurant() {
        System.out.println("---------------------------- Debut test en erreur");

        authenticateAsAdmin();
        final Long foodSpecialtyId = facade.createFoodSpecialty(TestUtils.validFoodSpecialty());
        final FoodSpecialty foodSpecialty = facade.readFoodSpecialty(foodSpecialtyId);
        assertNotNull(foodSpecialty);
        // Given
        final User user = TestUtils.validUser();
        final Long userId = facade.createAccount(user);
        final Restaurant restaurant = TestUtils.validRestaurant();
        restaurant.clearSpecialties();
        restaurant.addSpecialty(foodSpecialty);
        final Long restaurantId = facade.createRestaurant(userId, restaurant);
        assertNotNull(restaurantId);
        System.out.println("---------------------------- Fin test en erreur");

    }

    @Test
    public void persistingAccountWithRemovedRestaurantFromCollectionShouldDeleteRestaurant() {}

    @Test
    public void persistingAccountWithUpdatedRestaurantInCollectionShouldUpdateRestaurant() {}

    @Test
    public void updateAccountShouldSucceed() {
        authenticateAsAdmin();
        String email;
        User user;
        Long id;

        user = TestUtils.validUser();
        email = "first@email.org";
        user.setEmail(email);
        id = facade.createAccount(user);
        user = facade.readUser(id);
        Assert.assertEquals(email, user.getEmail());

        email = "second@email.org";
        user.setEmail(email);
        facade.updateAccount(user);
        user = facade.readUser(id);
        Assert.assertEquals(email, user.getEmail());
    }

    @Test
    public void updateEntityShouldPersistProperties() throws Throwable {
        authenticateAsAdmin();
        // Given
        FoodSpecialty foodSpecialty = TestUtils.validFoodSpecialty();
        foodSpecialty.setId(null);
        // When
        final Long id = facade.createFoodSpecialty(foodSpecialty);
        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, foodSpecialty.getId());
        final String newCode = "New Code";
        final String newLabel = "Brand New Code";

        // Given
        foodSpecialty.setCode(newCode);
        foodSpecialty.setLabel(newLabel);

        // When
        facade.updateFoodSpecialty(foodSpecialty);

        foodSpecialty = facade.readFoodSpecialty(id);

        // Then
        Assert.assertEquals(newCode, foodSpecialty.getCode());
        Assert.assertEquals(newLabel, foodSpecialty.getLabel());

    }
}
