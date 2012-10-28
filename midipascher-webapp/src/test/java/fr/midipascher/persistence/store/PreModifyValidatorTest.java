package fr.midipascher.persistence.store;

import com.google.common.collect.Sets;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;
import org.hibernate.event.spi.AbstractPreDatabaseOperationEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class PreModifyValidatorTest {
    @Mock
    private Validator validator;

    @InjectMocks
    private PreModifyValidator underTest;

    @Test(expected = IllegalArgumentException.class)
    public void validateShouldThrowIllegalArgumentExceptionWithNullEventEntity() throws Exception {
        AbstractPreDatabaseOperationEvent event = mock(AbstractPreDatabaseOperationEvent.class);
        Restaurant eventEntity = null;
        when(event.getEntity()).thenReturn(eventEntity);
        ValidationContext context = ValidationContext.DELETE;
        underTest.validate(eventEntity, context);
        verify(event).getEntity();
        verifyNoMoreInteractions(event);
        verifyZeroInteractions(eventEntity, validator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateShouldThrowIllegalArgumentExceptionWithNullValidationContext() throws Exception {
        AbstractPreDatabaseOperationEvent event = mock(AbstractPreDatabaseOperationEvent.class);
        Restaurant eventEntity = mock(Restaurant.class);
        when(event.getEntity()).thenReturn(eventEntity);
        ValidationContext context = null;
        underTest.validate(eventEntity, context);
        verify(event).getEntity();
        verifyNoMoreInteractions(event);
        verifyZeroInteractions(eventEntity, validator);
    }

    @Test(expected = ConstraintViolationException.class)
    public void validateShouldThrowConstraintViolationException() throws Exception {
        AbstractPreDatabaseOperationEvent event = mock(AbstractPreDatabaseOperationEvent.class);
        Restaurant eventEntity = mock(Restaurant.class);
        when(event.getEntity()).thenReturn(eventEntity);
        ValidationContext context = ValidationContext.CREATE;
        Class<?>[] groups = context.getContext();
        ConstraintViolation<Restaurant> constraintViolation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<Restaurant>> violations = Sets.newHashSet(constraintViolation);
        when(validator.validate(eventEntity, groups)).thenReturn(violations);
        underTest.validate(eventEntity, context);
        verify(event).getEntity();
        verify(validator).validate(eventEntity, groups);
        verifyNoMoreInteractions(event, validator);
        verifyZeroInteractions(eventEntity);
    }

}
