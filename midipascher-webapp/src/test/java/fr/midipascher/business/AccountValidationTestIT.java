/*
 *
 */
package fr.midipascher.business;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.test.TestFixtures;

/**
 * {@link Account} validation testing<br/>
 * 
 * @author louis.gueye@gmail.com
 */
public class AccountValidationTestIT extends BaseValidations {

    private Account underTest = null;

    @Test
    public void shouldValidateAccountForCreateContext() {

        shouldValidateFirstNameRequiredConstraint(ValidationContext.CREATE);

        shouldValidateFirstNameSizeConstraint(ValidationContext.CREATE);

        shouldValidateLastNameRequiredConstraint(ValidationContext.CREATE);

        shouldValidateLastNameSizeConstraint(ValidationContext.CREATE);

        shouldValidateEmailRequiredConstraint(ValidationContext.CREATE);

        shouldValidateEmailFormatConstraint(ValidationContext.CREATE);

        shouldValidatePasswordRequiredConstraint(ValidationContext.CREATE);

        shouldValidatePasswordSizeConstraint(ValidationContext.CREATE);

        shouldValidateAuthoritiesSizeConstraint(ValidationContext.CREATE);

    }

    @Test
    public void shouldValidateAccountForUpdateContext() {

        shouldValidateFirstNameRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateFirstNameSizeConstraint(ValidationContext.UPDATE);

        shouldValidateLastNameRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateLastNameSizeConstraint(ValidationContext.UPDATE);

        shouldValidateEmailRequiredConstraint(ValidationContext.UPDATE);

        shouldValidateEmailFormatConstraint(ValidationContext.UPDATE);

        shouldValidatePasswordRequiredConstraint(ValidationContext.UPDATE);

        shouldValidatePasswordSizeConstraint(ValidationContext.UPDATE);

        shouldValidateAuthoritiesSizeConstraint(ValidationContext.UPDATE);

    }

    /**
     * @param context
     */
    private void shouldValidateAuthoritiesSizeConstraint(final ValidationContext context) {
        Set<Authority> wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = null;
        underTest.setAuthorities(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "One authority is required", "authorities");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Un rôle est requis", "authorities");

        underTest = TestFixtures.validAccount();
        wrongData = Collections.emptySet();
        underTest.setAuthorities(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "One authority is required", "authorities");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Un rôle est requis", "authorities");
    }

    /**
     * @param context
     */
    private void shouldValidateEmailFormatConstraint(final ValidationContext context) {
        // Given
        underTest = TestFixtures.validAccount();
        final String wrongData = "toto.com";
        underTest.setEmail(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Valid email format is required", "email");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Un format valide d'email est requis", "email");

    }

    /**
     * @param context
     */
    private void shouldValidateEmailRequiredConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = null;
        underTest.setEmail(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Email is required", "email");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "L'email est requis", "email");

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = "";
        underTest.setEmail(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Email is required", "email");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "L'email est requis", "email");

    }

    /**
     * @param context
     */
    private void shouldValidateFirstNameRequiredConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = null;
        underTest.setFirstName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "First name is required", "firstName");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le prénom est requis", "firstName");

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = "";
        underTest.setFirstName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "First name is required", "firstName");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le prénom est requis", "firstName");

    }

    /**
     * @param context
     */
    private void shouldValidateFirstNameSizeConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = RandomStringUtils.random(Account.CONSTRAINT_FIRST_NAME_MAX_SIZE + 1);
        underTest.setFirstName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "First name max length is "
            + Account.CONSTRAINT_FIRST_NAME_MAX_SIZE, "firstName");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le prénom : "
            + Account.CONSTRAINT_FIRST_NAME_MAX_SIZE, "firstName");
    }

    /**
     * @param context
     */
    private void shouldValidateLastNameRequiredConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = null;
        underTest.setLastName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Last name is required", "lastName");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le nom est requis", "lastName");

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = "";
        underTest.setLastName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Last name is required", "lastName");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le nom est requis", "lastName");
    }

    /**
     * @param context
     */
    private void shouldValidateLastNameSizeConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = RandomStringUtils.random(Account.CONSTRAINT_LAST_NAME_MAX_SIZE + 1);
        underTest.setLastName(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Last name max length is "
            + Account.CONSTRAINT_FIRST_NAME_MAX_SIZE, "lastName");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le nom : "
            + Account.CONSTRAINT_FIRST_NAME_MAX_SIZE, "lastName");
    }

    /**
     * @param context
     */
    private void shouldValidatePasswordRequiredConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = null;
        underTest.setPassword(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Password is required", "password");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le mot de passe est requis", "password");

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = "";
        underTest.setPassword(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Password is required", "password");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Le mot de passe est requis", "password");
    }

    /**
     * @param context
     */
    private void shouldValidatePasswordSizeConstraint(final ValidationContext context) {
        String wrongData;

        // Given
        underTest = TestFixtures.validAccount();
        wrongData = RandomStringUtils.random(Account.CONSTRAINT_PASSWORD_MAX_SIZE + 1);
        underTest.setPassword(wrongData);

        assertExpectedViolation(underTest, context, Locale.ENGLISH, "Password max length is "
            + Account.CONSTRAINT_PASSWORD_MAX_SIZE, "password");
        assertExpectedViolation(underTest, context, Locale.FRENCH, "Longueur max pour le mot de passe : "
            + Account.CONSTRAINT_PASSWORD_MAX_SIZE, "password");
    }
}
