package fr.midipascher.persistence.store;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;

import org.hibernate.event.spi.PreUpdateEvent;
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

    @Mock
    private Geocoder geocoder;

    @InjectMocks
    private PreUpdateEventListener underTest;

    @Test
    public void onPreUpdateShouldSucceed() throws Exception {
        PreUpdateEvent event = mock(PreUpdateEvent.class);
        Restaurant eventEntity = mock(Restaurant.class);
        when(event.getEntity()).thenReturn(eventEntity);
        final Address address = mock(Address.class);
        when(eventEntity.getAddress()).thenReturn(address);
        boolean result = underTest.onPreUpdate(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.UPDATE);
        verify(geocoder).latLong(address);
        verify(eventEntity).setUpdated(Matchers.<DateTime>any());
        verify(eventEntity).getAddress();
        assertFalse(result);
        verifyNoMoreInteractions(event, eventEntity, preModifyValidator);
    }

    @Test
    public void onPreUpdateShouldNotSetCreatedDateWithNonEventAwareEntity() throws Exception {
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
  
    @Test
    public void onPreUpdateShouldNotGeocodeWithNonLocationAwareEntity() throws Exception {
        PreUpdateEvent event = mock(PreUpdateEvent.class);
        FoodSpecialty eventEntity = mock(FoodSpecialty.class);
        when(event.getEntity()).thenReturn(eventEntity);
        boolean result = underTest.onPreUpdate(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.UPDATE);
        assertFalse(result);
        verifyNoMoreInteractions(event, preModifyValidator);
        verifyZeroInteractions(eventEntity, geocoder);
    }

}
