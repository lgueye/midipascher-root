/*
 *
 */
package fr.midipascher.business;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestFixtures;

/**
 * {@link Restaurant} validation testing<br/>
 * CRUD operations are tested<br>
 * 
 * @author louis.gueye@gmail.com
 */
public class RestaurantValidationTestIT extends BaseValidations {

    private Restaurant underTest = null;

    /**
     * Given : a valid restaurant valued with an invalid address<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateAddressRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final Address wrongData = null;
        underTest.setAddress(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Location is required", "address");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "La localisation est requise", "address");

    }

    /**
     * Given : a valid restaurant valued with an invalid city<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCityRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.getAddress().setCity(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "City is required", "address.city");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "La ville est requise", "address.city");

    }

    /**
     * Given : a valid restaurant valued with an invalid city<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCitySizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_CITY_MAX_SIZE + 1);
        underTest.getAddress().setCity(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "City max length is "
            + Address.CONSTRAINT_CITY_MAX_SIZE, "address.city");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour la ville : "
            + Address.CONSTRAINT_CITY_MAX_SIZE, "address.city");

    }

    /**
     * Given : a valid restaurant valued with an invalid company id<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCompanyIdRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.setCompanyId(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Company identifier is required", "companyId");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le SIREN est requis", "companyId");

    }

    /**
     * Given : a valid restaurant valued with an invalid companyId<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCompanyIdSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_COMPANY_ID_MAX_SIZE + 1);
        underTest.setCompanyId(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Company identifier max length is "
            + Restaurant.CONSTRAINT_COMPANY_ID_MAX_SIZE, "companyId");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le SIREN : "
            + Restaurant.CONSTRAINT_COMPANY_ID_MAX_SIZE, "companyId");

    }

    /**
     * Given : a valid restaurant valued with an invalid country code<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCountryCodeRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.getAddress().setCountryCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Country code is required", "address.countryCode");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le code pays est requis", "address.countryCode");

    }

    /**
     * Given : a valid restaurant valued with an invalid country code<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateCountryCodeSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE + 1);
        underTest.getAddress().setCountryCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Country code exact length is "
            + Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE, "address.countryCode");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur exacte pour le code pays : "
            + Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE, "address.countryCode");

    }

    /**
     * Given : a valid restaurant valued with an invalid description<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateDescriptionSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE + 1);
        underTest.setDescription(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Description max length is "
            + Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE, "description");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour la description : "
            + Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE, "description");

    }

    /**
     * Given : a valid restaurant valued with an invalid main offer<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateMainOfferSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE + 1);
        underTest.setMainOffer(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Main offer max length is "
            + Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE, "mainOffer");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour l'offre best-seller : "
            + Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE, "mainOffer");

    }

    /**
     * Given : a valid restaurant valued with an invalid name<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateNameRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.setName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Name is required", "name");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le nom est requis", "name");

    }

    /**
     * Given : a valid restaurant valued with an invalid name<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateNameSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_NAME_MAX_SIZE + 1);
        underTest.setName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Name max length is "
            + Restaurant.CONSTRAINT_NAME_MAX_SIZE, "name");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le nom : "
            + Restaurant.CONSTRAINT_NAME_MAX_SIZE, "name");

    }

    /**
     * Given : a valid restaurant valued with an invalid phone number<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidatePhoneNumberRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.setPhoneNumber(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Phone number is required", "phoneNumber");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le numéro de téléphone est requis", "phoneNumber");

    }

    /**
     * Given : a valid restaurant valued with an invalid phone number<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidatePhoneNumberSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE + 1);
        underTest.setPhoneNumber(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Phone number max length is "
            + Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE, "phoneNumber");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le numéro de téléphone : "
            + Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE, "phoneNumber");

    }

    /**
     * Given : a valid restaurant valued with an invalid postal code<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidatePostalCodeRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.getAddress().setPostalCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Postal code is required", "address.postalCode");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le code postal est requis", "address.postalCode");

    }

    /**
     * Given : a valid restaurant valued with an invalid postal code<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidatePostalCodeSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE + 1);
        underTest.getAddress().setPostalCode(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Postal code max length is "
            + Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE, "address.postalCode");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le code postal : "
            + Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE, "address.postalCode");

    }

    /**
     * Validate "create restaurant use case"
     */
    @Test
    public void shouldValidateRestaurantForCreateContext() {

        shouldValidateNameRequiredConstraint(ValidationContext.CREATE);

        shouldValidateNameSizeConstraint(ValidationContext.CREATE);

        shouldValidateDescriptionSizeConstraint(ValidationContext.CREATE);

        shouldValidateCompanyIdRequiredConstraint(ValidationContext.CREATE);

        shouldValidateCompanyIdSizeConstraint(ValidationContext.CREATE);

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

        shouldValidateCompanyIdRequiredConstraint(ValidationContext.CREATE);

        shouldValidateCompanyIdSizeConstraint(ValidationContext.CREATE);

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
     * Given : a valid restaurant valued with invalid specialties list<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateSpecialtiesRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final Set<FoodSpecialty> wrongData = null;
        underTest.setSpecialties(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "At least one specialty should be set",
            "specialties");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Au moins une spécialité est requise", "specialties");

    }

    /**
     * Given : a valid restaurant valued with an invalid street address<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateStreetAddressRequiredConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = null;
        underTest.getAddress().setStreetAddress(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Street address is required",
            "address.streetAddress");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "L'adresse postale est requise",
            "address.streetAddress");

    }

    /**
     * Given : a valid restaurant valued with an invalid street address<br/>
     * When : one persists the above restaurant<br/>
     * Then : system should throw a {@link ConstraintViolationException}<br/>
     */
    private void shouldValidateStreetAddressSizeConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validRestaurant();
        final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE + 1);
        underTest.getAddress().setStreetAddress(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Street address max length is "
            + Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE, "address.streetAddress");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour l'adresse postale : "
            + Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE, "address.streetAddress");

    }
}
