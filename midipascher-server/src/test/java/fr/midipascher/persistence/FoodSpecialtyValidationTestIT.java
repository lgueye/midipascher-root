/*
 *
 */
package fr.midipascher.persistence;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestUtils;

/**
 * FoodSpecialty validation testing<br/>
 * CRUD operations are tested<br>
 * 
 * @author louis.gueye@gmail.com
 */
public class FoodSpecialtyValidationTestIT extends BasePersistenceTestIT {

	private FoodSpecialty	underTest	= null;

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
	 * Given : a valid food specialty valued with an invalid code<br/>
	 * When : one persists the above food specialty<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCodeRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validFoodSpecialty();
		final String wrongData = null;
		this.underTest.setCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{foodSpecialty.code.required}", "code");

	}

	/**
	 * Given : a valid food specialty valued with an invalid code<br/>
	 * When : one persists the above food specialty<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCodeSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validFoodSpecialty();
		final String wrongData = RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE + 1);
		this.underTest.setCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{foodSpecialty.code.max.size}", "code");

	}

	/**
	 * Given : a valid food specialty valued with an invalid label<br/>
	 * When : one persists the above food specialty<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateLabelRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validFoodSpecialty();
		final String wrongData = null;
		this.underTest.setLabel(wrongData);

		assertExpectedViolation(this.underTest, context, "{foodSpecialty.label.required}", "label");

	}

	/**
	 * Given : a valid food specialty valued with an invalid label<br/>
	 * When : one persists the above food specialty<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateLabelSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validFoodSpecialty();
		final String wrongData = RandomStringUtils.random(FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE + 1);
		this.underTest.setLabel(wrongData);

		assertExpectedViolation(this.underTest, context, "{foodSpecialty.label.max.size}", "label");

	}

}
