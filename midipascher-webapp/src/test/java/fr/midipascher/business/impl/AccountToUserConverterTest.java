/**
 * 
 */
package fr.midipascher.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountToUserConverterTest {

	@Mock
	private AuthorityToGrantedAuthorityConverter	authorityToGrantedAuthorityConverter;

	@InjectMocks
	private final Converter<Account, User>			underTest	= new AccountToUserConverter();

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AccountToUserConverter#convert(fr.midipascher.domain.Account)}
	 * .
	 */
	@Test
	public final void convertShouldReturnNullWithNullInput() {

		// Variables
		Account account;
		User user;

		// Given
		account = null;

		// When
		user = this.underTest.convert(account);

		// Then
		assertNull(user);

	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AccountToUserConverter#convert(fr.midipascher.domain.Account)}
	 * .
	 */
	@Test
	public final void convertShouldSucceed() {

		// Variables
		Account account;
		User user;
		String email;
		String password;
		boolean locked;

		// Given
		account = Mockito.mock(Account.class);
		email = "mail@mail.com";
		Mockito.when(account.getEmail()).thenReturn(email);
		password = "secret";
		Mockito.when(account.getPassword()).thenReturn(password);
		locked = false;
		Mockito.when(account.isLocked()).thenReturn(locked);
		Set<Authority> authorities = Sets.newHashSet();
		Mockito.when(account.getAuthorities()).thenReturn(authorities);
		Collection<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
		Mockito.when(this.authorityToGrantedAuthorityConverter.fromAuthorities(authorities)).thenReturn(
				grantedAuthorities);

		// When
		user = this.underTest.convert(account);

		// Then
		assertNotNull(user);
		assertEquals(email, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(grantedAuthorities, Lists.newArrayList(user.getAuthorities()));
		assertEquals(!locked, user.isEnabled());
		assertEquals(!locked, user.isAccountNonLocked());
		assertEquals(!locked, user.isAccountNonExpired());
		assertEquals(!locked, user.isCredentialsNonExpired());

	}

}
