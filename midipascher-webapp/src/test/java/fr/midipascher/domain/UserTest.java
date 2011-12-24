/**
 * 
 */
package fr.midipascher.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author louis.gueye@gmail.com
 */
public class UserTest {

	private User	underTest;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.underTest = new User();
	}

	@Test
	public void clearAuthoritiesWillIgnoreIfNullAuthorities() {
		this.underTest.setAuthorities(null);
		this.underTest.clearAuthorities();
		Assert.assertNull(this.underTest.getAuthorities());
	}

	@Test
	public void clearAuthoritiesWillSucceed() {
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(new Authority());
		this.underTest.setAuthorities(authorities);
		this.underTest.clearAuthorities();
		Assert.assertNotNull(this.underTest.getAuthorities());
		Assert.assertTrue(CollectionUtils.sizeIsEmpty(this.underTest.getAuthorities()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addAuthorityWillThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		Authority authority = null;

		// When
		this.underTest.addAuthority(authority);
	}

	@Test
	public void addAuthorityWillSucceed() {
		int authoritiesCount = this.underTest.getAuthorities() == null ? 0 : this.underTest.getAuthorities().size();
		this.underTest.addAuthority(new Authority());
		Assert.assertEquals(authoritiesCount + 1, CollectionUtils.size(this.underTest.getAuthorities()));
	}
}
