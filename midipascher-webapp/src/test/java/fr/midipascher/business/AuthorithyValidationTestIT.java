/*
 *
 */
package fr.midipascher.business;

import java.util.Locale;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fr.midipascher.domain.Authority;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestFixtures;

/**
 * Authority validation testing<br/>
 * CRUD operations are tested<br>
 * 
 * @author louis.gueye@gmail.com
 */
public class AuthorithyValidationTestIT extends BaseValidations {

    private Authority underTest = null;

    /**
     * Given : a valid food specialty valued with an invalid code<br/>
     * When : one creates the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    @Test
    public void shouldValidateAuthorityForCreateContext() {

        shouldValidateCodeRequiredConstraint(ValidationContext.CREATE);

        shouldValidateCodeSizeConstraint(ValidationContext.CREATE);

        shouldValidateLabelRequiredConstraint(ValidationContext.CREATE);

        shouldValidateLabelSizeConstraint(ValidationContext.CREATE);

    }

    /**
     * Given : a valid food specialty valued with an invalid code<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    @Test
    public void shouldValidateAuthorityForUpdateContext() {

        shouldValidateCodeRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateCodeSizeConstraint(ValidationContext.UPDATE);

        shouldValidateLabelRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateLabelSizeConstraint(ValidationContext.UPDATE);

    }

    /**
     * Given : a valid food specialty valued with an invalid code<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCodeRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validAuthority();
        final String wrongData = null;
        underTest.setCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Code is required", "code");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Code requis", "code");

    }

    /**
     * Given : a valid food specialty valued with an invalid code<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCodeSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validAuthority();
        final String wrongData = RandomStringUtils.random(Authority.CONSTRAINT_CODE_MAX_SIZE + 1);
        underTest.setCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Code max length is "
            + Authority.CONSTRAINT_CODE_MAX_SIZE, "code");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour code : "
            + Authority.CONSTRAINT_CODE_MAX_SIZE, "code");

    }

    /**
     * Given : a valid food specialty valued with an invalid label<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateLabelRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validAuthority();
        final String wrongData = null;
        underTest.setLabel(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Label is required", "label");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Label requis", "label");

    }

    /**
     * Given : a valid food specialty valued with an invalid label<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateLabelSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validAuthority();
        final String wrongData = RandomStringUtils.random(Authority.CONSTRAINT_LABEL_MAX_SIZE + 1);
        underTest.setLabel(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Label max length is "
            + Authority.CONSTRAINT_LABEL_MAX_SIZE, "label");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour label : "
            + Authority.CONSTRAINT_LABEL_MAX_SIZE, "label");

    }

}
