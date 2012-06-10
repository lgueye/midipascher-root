/**
 * 
 */
package fr.midipascher.business.impl;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.User;

import fr.midipascher.domain.Account;

/**
 * @author louis.gueye@gmail.com
 */
public class AccountToUserConverterTest {

	private Converter<Account, User>	underTest;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.underTest = new AccountToUserConverter();
	}

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

		// When
		user = this.underTest.convert(account);

		// Then
		assertNull(user);

	}

}
