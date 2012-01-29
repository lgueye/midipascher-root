/*
 *
 */
package fr.midipascher.persistence;

import javax.validation.Validator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Authority database integration testing<br/>
 * CRUD operations are tested<br>
 * Finders are tested<br/>
 * 
 * @author louis.gueye@gmail.com
 */
public class ValidatorTestIT extends BasePersistenceTestIT {

	@Autowired
	private Validator	underTest;

	@Test
	public final void validatorShouldConsiderLocale() {
		// FoodSpecialty foodSpecialty;
		//
		// // Given
		// foodSpecialty = TestUtils.validFoodSpecialty();
		// foodSpecialty.setCode(RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE
		// + 1,
		// TestUtils.STANDARD_CHARSET));
		// try {
		// this.underTest.validate(foodSpecialty, Create.class);
		// fail(ConstraintViolationException.class.getSimpleName() +
		// " expected");
		// } catch (Throwable th) {
		// assertTrue((th instanceof ConstraintViolationException));
		// ConstraintViolationException violations =
		// (ConstraintViolationException) th;
		// assertTrue(violations.getConstraintViolations().size() == 1);
		// ConstraintViolation<FoodSpecialty> violation =
		// (ConstraintViolation<FoodSpecialty>)
		// violations.getConstraintViolations().iterator().next();
		// assertEquals("code", violation.getPropertyPath().toString());
		// }
	}

}
