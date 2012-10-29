package fr.midipascher.persistence.store;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.LocationAware;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.validation.ValidationContext;
import org.hibernate.event.spi.PreInsertEvent;
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
 * louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class PreInsertEventListenerTest {

    @Mock
    private PreModifyValidator preModifyValidator;

    @Mock
    private Geocoder geocoder;

    @InjectMocks
    private PreInsertEventListener underTest;

    @Test
    public void onPreInsertShouldSucceed() throws Exception {
        PreInsertEvent event = mock(PreInsertEvent.class);
        Restaurant eventEntity = mock(Restaurant.class);
        when(event.getEntity()).thenReturn(eventEntity);
        final Address address = mock(Address.class);
        when(eventEntity.getAddress()).thenReturn(address);
        boolean result = underTest.onPreInsert(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.CREATE);
        verify(geocoder).latLong(address);
        verify(eventEntity).setCreated(Matchers.<DateTime>any());
        verify(eventEntity).getAddress();
        assertFalse(result);
        verifyNoMoreInteractions(event, eventEntity, preModifyValidator);
    }
    
    @Test
    public void onPreInsertShouldNotSetCreatedDateWithNonEventAwareEntity() throws Exception {
        PreInsertEvent event = mock(PreInsertEvent.class);
        FoodSpecialty eventEntity = mock(FoodSpecialty.class);
        when(event.getEntity()).thenReturn(eventEntity);
        boolean result = underTest.onPreInsert(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.CREATE);
        assertFalse(result);
        verifyNoMoreInteractions(event, preModifyValidator);
        verifyZeroInteractions(eventEntity);
    }

    @Test
    public void onPreInsertShouldNotGeocodeWithNonLocationAwareEntity() throws Exception {
        PreInsertEvent event = mock(PreInsertEvent.class);
        FoodSpecialty eventEntity = mock(FoodSpecialty.class);
        when(event.getEntity()).thenReturn(eventEntity);
        boolean result = underTest.onPreInsert(event);
        verify(event).getEntity();
        verify(preModifyValidator).validate(eventEntity, ValidationContext.CREATE);
        assertFalse(result);
        verifyNoMoreInteractions(event, preModifyValidator);
        verifyZeroInteractions(eventEntity, geocoder);
    }

}
