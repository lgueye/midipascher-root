/**
 * 
 */
package fr.midipascher.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Sets;

import fr.midipascher.domain.Authority;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorityToGrantedAuthorityConverterTest {

	private AuthorityToGrantedAuthorityConverter	underTest;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.underTest = new AuthorityToGrantedAuthorityConverter();
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AuthorityToGrantedAuthorityConverter#convert(fr.midipascher.domain.Authority)}
	 * .
	 */
	@Test
	public final void convertShouldReturnNullWithNullInput() {

		// Variables
		Authority source;
		GrantedAuthority grantedAuthority;

		// Given
		source = null;

		// When
		grantedAuthority = this.underTest.convert(source);

		// Then
		assertNull(grantedAuthority);

	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AuthorityToGrantedAuthorityConverter#convert(fr.midipascher.domain.Authority)}
	 * .
	 */
	@Test
	public final void convertShouldSucceed() {

		// Variables
		Authority source;
		GrantedAuthority grantedAuthority;
		String code;

		// Given
		source = Mockito.mock(Authority.class);
		code = "adm";
		Mockito.when(source.getCode()).thenReturn(code);

		// When
		grantedAuthority = this.underTest.convert(source);

		// Then
		assertNotNull(grantedAuthority);
		assertEquals(Authority.ROLE_PREFIX + code.toUpperCase(), grantedAuthority.getAuthority());

	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AuthorityToGrantedAuthorityConverter#fromAuthorities(java.util.Set)}
	 * .
	 */
	@Test
	public final void fromAuthoritiesShouldReturnAnEmptyCollectionWithNullInput() {

		// Variables
		Set<Authority> authorities;
		Collection<GrantedAuthority> grantedAuthorities;

		// Given
		authorities = null;

		// When
		grantedAuthorities = this.underTest.fromAuthorities(authorities);

		// Then
		assertNotNull(grantedAuthorities);
		assertTrue(grantedAuthorities.size() == 0);

	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AuthorityToGrantedAuthorityConverter#fromAuthorities(java.util.Set)}
	 * .
	 */
	@Test
	public final void fromAuthoritiesShouldReturnAnEmptyCollectionWithEmptyInput() {

		// Variables
		Set<Authority> authorities;
		Collection<GrantedAuthority> grantedAuthorities;

		// Given
		authorities = Sets.newHashSet();

		// When
		grantedAuthorities = this.underTest.fromAuthorities(authorities);

		// Then
		assertNotNull(grantedAuthorities);
		assertTrue(grantedAuthorities.size() == 0);

	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AuthorityToGrantedAuthorityConverter#fromAuthorities(java.util.Set)}
	 * .
	 */
	@Test
	public final void fromAuthoritiesShouldNotAddNullConvertedElements() {

		// Variables
		Set<Authority> authorities;
		Authority authority;
		Collection<GrantedAuthority> grantedAuthorities;

		// Given
		authorities = Sets.newHashSet();
		authority = null;
		authorities.add(authority);

		// When
		grantedAuthorities = this.underTest.fromAuthorities(authorities);

		// Then
		assertNotNull(grantedAuthorities);
		assertTrue(grantedAuthorities.size() == 0);

	}

	/**
	 * Test method for
	 * {@link fr.midipascher.business.impl.AuthorityToGrantedAuthorityConverter#fromAuthorities(java.util.Set)}
	 * .
	 */
	@Test
	public final void fromAuthoritiesShouldSucceed() {

		// Variables
		Set<Authority> authorities;
		Authority authority;
		Collection<GrantedAuthority> grantedAuthorities;
		String code;

		// Given
		authorities = Sets.newHashSet();
		authority = Mockito.mock(Authority.class);
		code = "rmgr";
		Mockito.when(authority.getCode()).thenReturn(code);

		authorities.add(authority);

		// When
		grantedAuthorities = this.underTest.fromAuthorities(authorities);

		// Then
		assertNotNull(grantedAuthorities);
		assertTrue(grantedAuthorities.size() == 1);

	}

}
