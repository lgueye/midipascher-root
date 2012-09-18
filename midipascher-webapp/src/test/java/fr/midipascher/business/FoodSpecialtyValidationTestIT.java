/*
 *
 */
package fr.midipascher.business;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestFixtures;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.util.Locale;

/**
 * FoodSpecialty validation testing<br/>
 * CRUD operations are tested<br>
 *
 * @author louis.gueye@gmail.com
 */
public class FoodSpecialtyValidationTestIT extends BaseValidations {

    private FoodSpecialty underTest = null;

    /**
     * Given : a valid food specialty valued with an invalid code<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCodeRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validFoodSpecialty();
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
        underTest = TestFixtures.validFoodSpecialty();
        final String wrongData = RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE + 1);
        underTest.setCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Code max length is "
                + FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE, "code");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour code : "
                + FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE, "code");

    }

    /**
     * Given : a valid food specialty valued with an invalid code<br/>
     * When : one creates the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    @Test
    public void shouldValidateFoodSpecialtyForCreateContext() {

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
    public void shouldValidateFoodSpecialtyForUpdateContext() {

        shouldValidateCodeRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateCodeSizeConstraint(ValidationContext.UPDATE);

        shouldValidateLabelRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateLabelSizeConstraint(ValidationContext.UPDATE);

    }

    /**
     * Given : a valid food specialty valued with an invalid label<br/>
     * When : one persists the above food specialty<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateLabelRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validFoodSpecialty();
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
        underTest = TestFixtures.validFoodSpecialty();
        final String wrongData = RandomStringUtils.random(FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE + 1);
        underTest.setLabel(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Label max length is "
                + FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE, "label");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour label : "
                + FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE, "label");

    }

}
