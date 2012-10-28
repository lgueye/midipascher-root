package fr.midipascher.persistence.store;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;
import org.hibernate.event.spi.PreUpdateEvent;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * User: lgueye Date: 24/10/12 Time: 19:30
 */
@RunWith(MockitoJUnitRunner.class)
public class PreUpdateEventListenerTest {

    @Mock
    private PreModifyValidator preModifyValidator;

    @InjectMocks
    private PreUpdateEventListener underTest;

    @Test
    public void onPreUpdateShouldSucceed() throws Exception {
        PreUpdateEvent event = mock(PreUpdateEvent.class);
        Restaurant eventEntity = mock(Restaurant.class);
        when(event.getEntity()).thenReturn(eventEntity);
        boolean result = underTest.onPreUpdate(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.UPDATE);
        verify(eventEntity).setUpdated(Matchers.<DateTime>any());
        assertFalse(result);
        verifyNoMoreInteractions(event, eventEntity, preModifyValidator);
    }

    @Test
    public void onPreUpdateShouldNotSetUpdatedDateWithNonEventAwareEntity() throws Exception {
        PreUpdateEvent event = mock(PreUpdateEvent.class);
        FoodSpecialty eventEntity = mock(FoodSpecialty.class);
        when(event.getEntity()).thenReturn(eventEntity);
        boolean result = underTest.onPreUpdate(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.UPDATE);
        assertFalse(result);
        verifyNoMoreInteractions(event, preModifyValidator);
        verifyZeroInteractions(eventEntity);
    }

}
