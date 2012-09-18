/**
 *
 */
package fr.midipascher.business.impl;

import com.google.common.collect.Lists;
import fr.midipascher.domain.Account;
import fr.midipascher.persistence.BaseDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private BaseDao baseDao;

    @Mock
    private AccountToUserConverter accountToUserConverter;

    @InjectMocks
    private final UserDetailsServiceImpl underTest = new UserDetailsServiceImpl();

    /**
     * Test method for {@link fr.midipascher.business.impl.UserDetailsServiceImpl#loadUserByUsername(java.lang.String)}
     * .
     */
    @Test
    public final void loadUserByUsernameShouldSucceed() {

        // Variables
        String username;
        User user;
        Account account;

        // Given
        username = "louis@mail.com";
        account = Mockito.mock(Account.class);
        user = Mockito.mock(User.class);
        List<Account> accounts = Lists.newArrayList();
        accounts.add(account);
        Mockito.when(this.baseDao.findByExample(Matchers.any(Account.class))).thenReturn(accounts);
        Mockito.when(this.accountToUserConverter.convert(account)).thenReturn(user);

        // When
        UserDetails result = this.underTest.loadUserByUsername(username);

        // Then
        assertSame(result, user);

    }

}
