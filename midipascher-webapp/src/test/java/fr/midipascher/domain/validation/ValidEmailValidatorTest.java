/**
 * 
 */
package fr.midipascher.domain.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(Theories.class)
public class ValidEmailValidatorTest {

	private ValidEmailValidator		underTest;

	@DataPoints
	public static final String[]	VALID_EMAILS	= { null, "", "a@company.com", "a.b@company.com",
			"a.b@company.com", "update@me.com"		};

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.underTest = new ValidEmailValidator();
	}

	@Theory
	public void shouldValidate(String validEmail) {
		Assert.assertTrue(this.underTest.isValid(validEmail, null));
	}
}
