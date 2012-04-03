/*
 *
 */
package fr.midipascher.business;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.midipascher.business.impl.ValidatorImpl;
import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.business.Validator;
import fr.midipascher.domain.validation.ValidationContext;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidatorImplTest {

    @Mock
    private javax.validation.Validator validator;

    @InjectMocks
    private final Validator underTest = new ValidatorImpl();

    /**
     * Test method for
     * {@link fr.midipascher.business.impl.ValidatorImpl#validate(fr.midipascher.domain.AbstractEntity, fr.midipascher.domain.validation.ValidationContext)}
     * .
     */
    @Test
    public final void validateShouldNotThrowConstraintViolationWithEmptyViolations() {

        // Variables
        AbstractEntity type;
        ValidationContext context;
        Set<ConstraintViolation<AbstractEntity>> violations;

        // Given
        type = Mockito.mock(FoodSpecialty.class);
        context = ValidationContext.UPDATE;
        violations = null;
        Mockito.when(validator.validate(type, context.getContext())).thenReturn(violations);

        // When
        underTest.validate(type, context);

        // Then
        Mockito.verify(validator).validate(type, context.getContext());
        Mockito.verifyNoMoreInteractions(validator);

        // Given
        type = Mockito.mock(FoodSpecialty.class);
        context = ValidationContext.UPDATE;
        violations = new HashSet<ConstraintViolation<AbstractEntity>>();
        Mockito.when(validator.validate(type, context.getContext())).thenReturn(violations);

        // When
        underTest.validate(type, context);

        // Then
        Mockito.verify(validator).validate(type, context.getContext());
        Mockito.verifyNoMoreInteractions(validator);

    }

    /**
     * Test method for
     * {@link fr.midipascher.business.impl.ValidatorImpl#validate(fr.midipascher.domain.AbstractEntity, fr.midipascher.domain.validation.ValidationContext)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test(expected = ConstraintViolationException.class)
    public final void validateShouldThrowConstraintViolationWithNonEmptyViolations() {

        // Variables
        AbstractEntity type;
        ValidationContext context;
        Set<ConstraintViolation<AbstractEntity>> violations;

        // Given
        type = Mockito.mock(FoodSpecialty.class);
        context = ValidationContext.UPDATE;
        violations = new HashSet<ConstraintViolation<AbstractEntity>>();
        violations.add(Mockito.mock(ConstraintViolation.class));
        Mockito.when(validator.validate(type, context.getContext())).thenReturn(violations);

        // When
        underTest.validate(type, context);

        // Then
        Mockito.verify(validator).validate(type, context.getContext());
        Mockito.verifyNoMoreInteractions(validator);

    }

    /**
     * Test method for
     * {@link fr.midipascher.business.impl.ValidatorImpl#validate(fr.midipascher.domain.AbstractEntity, fr.midipascher.domain.validation.ValidationContext)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public final void validateShouldThrowIllegalArgumentExceptionWithNullContext() {

        // Variables
        AbstractEntity type;
        ValidationContext context;

        // Given
        type = Mockito.mock(FoodSpecialty.class);
        context = null;

        // When
        underTest.validate(type, context);

    }

    /**
     * Test method for
     * {@link fr.midipascher.business.impl.ValidatorImpl#validate(fr.midipascher.domain.AbstractEntity, fr.midipascher.domain.validation.ValidationContext)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public final void validateShouldThrowIllegalArgumentExceptionWithNullType() {

        // Variables
        AbstractEntity type;
        ValidationContext context;

        // Given
        type = null;
        context = ValidationContext.UPDATE;

        // When
        underTest.validate(type, context);

    }

}
