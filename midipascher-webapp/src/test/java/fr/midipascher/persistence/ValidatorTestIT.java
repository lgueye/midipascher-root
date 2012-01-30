/*
 *
 */
package fr.midipascher.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.midipacher.TestConstants;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.validation.Create;
import fr.midipascher.test.TestUtils;

/**
 * Authority database integration testing<br/>
 * CRUD operations are tested<br>
 * Finders are tested<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { TestConstants.VALIDATION_CONTEXT })
public class ValidatorTestIT {

	@Autowired
	private Validator	underTest;

	@Test
	public final void validatorShouldConsiderLocale() {

		// Variables
		FoodSpecialty foodSpecialty;
		Set<ConstraintViolation<FoodSpecialty>> violations;
		ConstraintViolation<FoodSpecialty> violation;

		// Given
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		foodSpecialty = TestUtils.validFoodSpecialty();
		foodSpecialty.setCode(RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE + 1,
				TestUtils.STANDARD_CHARSET));

		// When
		violations = this.underTest.validate(foodSpecialty, Create.class);

		// Then
		assertTrue(violations.size() == 1);
		violation = violations.iterator().next();
		assertEquals("code", violation.getPropertyPath().toString());
		assertEquals("Code max length is 10", violation.getMessage());

		// Given
		LocaleContextHolder.setLocale(Locale.FRENCH);
		foodSpecialty = TestUtils.validFoodSpecialty();
		foodSpecialty.setCode(RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE + 1,
				TestUtils.STANDARD_CHARSET));

		// When
		violations = this.underTest.validate(foodSpecialty, Create.class);

		// Then
		assertTrue(violations.size() == 1);
		violation = violations.iterator().next();
		assertEquals("code", violation.getPropertyPath().toString());
		assertEquals("Longueur max pour code : 10", violation.getMessage());

	}

}
