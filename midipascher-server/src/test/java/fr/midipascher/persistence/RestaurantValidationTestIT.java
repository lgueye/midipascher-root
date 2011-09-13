/*
 *
 */
package fr.midipascher.persistence;

import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestUtils;

/**
 * Restaurant validation testing<br/>
 * CRUD operations are tested<br>
 * 
 * @author louis.gueye@gmail.com
 */
public class RestaurantValidationTestIT extends BasePersistenceTestIT {

	private Restaurant	underTest	= null;

	/**
	 * Validate "create restaurant use case"
	 */
	@Test
	public void shouldValidateRestaurantForCreateContext() {

		shouldValidateNameRequiredConstraint(ValidationContext.CREATE);

		shouldValidateNameSizeConstraint(ValidationContext.CREATE);

		shouldValidateDescriptionSizeConstraint(ValidationContext.CREATE);

		shouldValidateEmailRequiredConstraint(ValidationContext.CREATE);

		shouldValidateEmailValidFormatConstraint(ValidationContext.CREATE);

		shouldValidatePhoneNumberRequiredConstraint(ValidationContext.CREATE);

		shouldValidatePhoneNumberSizeConstraint(ValidationContext.CREATE);

		shouldValidateMainOfferSizeConstraint(ValidationContext.CREATE);

		shouldValidateAddressRequiredConstraint(ValidationContext.CREATE);

		shouldValidateStreetAddressRequiredConstraint(ValidationContext.CREATE);

		shouldValidateStreetAddressSizeConstraint(ValidationContext.CREATE);

		shouldValidateCityRequiredConstraint(ValidationContext.CREATE);

		shouldValidateCitySizeConstraint(ValidationContext.CREATE);

		shouldValidatePostalCodeRequiredConstraint(ValidationContext.CREATE);

		shouldValidatePostalCodeSizeConstraint(ValidationContext.CREATE);

		shouldValidateCountryCodeRequiredConstraint(ValidationContext.CREATE);

		shouldValidateCountryCodeSizeConstraint(ValidationContext.CREATE);

		shouldValidateSpecialtiesRequiredConstraint(ValidationContext.CREATE);

	}

	/**
	 * Validate "create restaurant use case"
	 */
	@Test
	public void shouldValidateRestaurantForUpdateContext() {

		shouldValidateNameRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateNameSizeConstraint(ValidationContext.UPDATE);

		shouldValidateDescriptionSizeConstraint(ValidationContext.UPDATE);

		shouldValidateEmailRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateEmailValidFormatConstraint(ValidationContext.UPDATE);

		shouldValidatePhoneNumberRequiredConstraint(ValidationContext.UPDATE);

		shouldValidatePhoneNumberSizeConstraint(ValidationContext.UPDATE);

		shouldValidateMainOfferSizeConstraint(ValidationContext.UPDATE);

		shouldValidateAddressRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateStreetAddressRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateStreetAddressSizeConstraint(ValidationContext.UPDATE);

		shouldValidateCityRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateCitySizeConstraint(ValidationContext.UPDATE);

		shouldValidatePostalCodeRequiredConstraint(ValidationContext.UPDATE);

		shouldValidatePostalCodeSizeConstraint(ValidationContext.UPDATE);

		shouldValidateCountryCodeRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateCountryCodeSizeConstraint(ValidationContext.UPDATE);

		shouldValidateSpecialtiesRequiredConstraint(ValidationContext.UPDATE);

	}

	/**
	 * Given : a valid restaurant valued with an invalid name<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateNameRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.setName(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.name.required}", "name");

	}

	/**
	 * Given : a valid restaurant valued with an invalid name<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateNameSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_NAME_MAX_SIZE + 1);
		this.underTest.setName(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.name.max.size}", "name");

	}

	/**
	 * Given : a valid restaurant valued with an invalid phone number<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePhoneNumberRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.setPhoneNumber(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.phoneNumber.required}", "phoneNumber");

	}

	/**
	 * Given : a valid restaurant valued with invalid specialties list<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateSpecialtiesRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final Set<FoodSpecialty> wrongData = null;
		this.underTest.setSpecialties(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.specialties.required}", "specialties");

	}

	/**
	 * Given : a valid restaurant valued with an invalid address<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateAddressRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final Address wrongData = null;
		this.underTest.setAddress(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.address.required}", "address");

	}

	/**
	 * Given : a valid restaurant valued with an invalid street address<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateStreetAddressRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.getAddress().setStreetAddress(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.streetAddress.required}", "address.streetAddress");

	}

	/**
	 * Given : a valid restaurant valued with an invalid street address<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateStreetAddressSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE + 1);
		this.underTest.getAddress().setStreetAddress(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.streetAddress.max.size}", "address.streetAddress");

	}

	/**
	 * Given : a valid restaurant valued with an invalid city<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCityRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.getAddress().setCity(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.city.required}", "address.city");

	}

	/**
	 * Given : a valid restaurant valued with an invalid city<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCitySizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_CITY_MAX_SIZE + 1);
		this.underTest.getAddress().setCity(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.city.max.size}", "address.city");

	}

	/**
	 * Given : a valid restaurant valued with an invalid postal code<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePostalCodeRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.getAddress().setPostalCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.postalCode.required}", "address.postalCode");

	}

	/**
	 * Given : a valid restaurant valued with an invalid postal code<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePostalCodeSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE + 1);
		this.underTest.getAddress().setPostalCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.postalCode.max.size}", "address.postalCode");

	}

	/**
	 * Given : a valid restaurant valued with an invalid country code<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCountryCodeRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.getAddress().setCountryCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.countryCode.required}", "address.countryCode");

	}

	/**
	 * Given : a valid restaurant valued with an invalid country code<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCountryCodeSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE + 1);
		this.underTest.getAddress().setCountryCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.countryCode.exact.size}", "address.countryCode");

	}

	/**
	 * Given : a valid restaurant valued with an invalid phone number<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePhoneNumberSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE + 1);
		this.underTest.setPhoneNumber(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.phoneNumber.max.size}", "phoneNumber");

	}

	/**
	 * Given : a valid restaurant valued with an invalid main offer<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateMainOfferSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE + 1);
		this.underTest.setMainOffer(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.mainOffer.max.size}", "mainOffer");

	}

	/**
	 * Given : a valid restaurant valued with an invalid description<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateDescriptionSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE + 1);
		this.underTest.setDescription(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.description.max.size}", "description");

	}

	/**
	 * Given : a valid restaurant valued with an invalid email<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateEmailRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = null;
		this.underTest.setEmail(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.email.required}", "email");

	}

	/**
	 * Given : a valid restaurant valued with an invalid email<br/>
	 * When : one persists the above restaurant<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateEmailValidFormatConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validRestaurant();
		final String wrongData = "foo.bar";
		this.underTest.setEmail(wrongData);

		assertExpectedViolation(this.underTest, context, "{restaurant.email.valid.format.required}", "email");

	}
}
