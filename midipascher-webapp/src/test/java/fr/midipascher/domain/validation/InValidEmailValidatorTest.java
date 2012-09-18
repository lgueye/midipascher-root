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
public class InValidEmailValidatorTest {

    private ValidEmailValidator underTest;

    @DataPoints
    public static final String[] INVALID_EMAILS = {"@company.com", "a.b@", "a.b"};

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.underTest = new ValidEmailValidator();
    }

    @Theory
    public void shouldNotValidate(String validEmail) {
        Assert.assertFalse(this.underTest.isValid(validEmail, null));
    }
}
