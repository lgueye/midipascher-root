package fr.midipascher.persistence.store;

import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.exceptions.BusinessException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class GeocoderTest {

  @Mock
  private com.google.code.geocoder.Geocoder geocoder;

  @InjectMocks
  private Geocoder underTest;

  @Test
  public void latLongShouldThrowBusinessExceptionWithNullResponse() throws Exception {

      Address address = mock(Address.class);
      String formattedAddress = "15 rue La Fayette, 75009 Paris, France";
      when(address.getFormattedAddress()).thenReturn(formattedAddress);
      GeocodeResponse geocodeResponse = null;
      when(geocoder.geocode(Matchers.<GeocoderRequest>any())).thenReturn(geocodeResponse);
      try {
          underTest.latLong(address);
          fail("BusinessException expected");
      } catch (BusinessException e) {
          assertEquals("geocode.service.unavailable", e.getMessageCode());
      }
      verify(address).getFormattedAddress();
      verify(geocoder, times(11)).geocode(Matchers.<GeocoderRequest>any());
      verifyNoMoreInteractions(address, geocoder);
  }

  @Test
  public void latLongShouldThrowBusinessExceptionWithNullResults() throws Exception {

      Address address = mock(Address.class);
      String formattedAddress = "15 rue La Fayette, 75009 Paris, France";
      when(address.getFormattedAddress()).thenReturn(formattedAddress);
      GeocodeResponse geocodeResponse = mock(GeocodeResponse.class);
      when(geocoder.geocode(Matchers.<GeocoderRequest>any())).thenReturn(geocodeResponse);
      List<GeocoderResult> results = null;
      when(geocodeResponse.getResults()).thenReturn(results);
      try {
          underTest.latLong(address);
          fail("BusinessException expected");
      } catch (BusinessException e) {
          assertEquals("geocode.no.results", e.getMessageCode());
      }
      verify(address).getFormattedAddress();
      verify(geocoder, times(11)).geocode(Matchers.<GeocoderRequest>any());
      verifyNoMoreInteractions(address, geocoder);
  }

  @Test
  public void latLongShouldThrowBusinessExceptionWithEmptyResults() throws Exception {

      Address address = mock(Address.class);
      String formattedAddress = "15 rue La Fayette, 75009 Paris, France";
      when(address.getFormattedAddress()).thenReturn(formattedAddress);
      GeocodeResponse geocodeResponse = mock(GeocodeResponse.class);
      when(geocoder.geocode(Matchers.<GeocoderRequest>any())).thenReturn(geocodeResponse);
      List<GeocoderResult> results = Lists.newArrayList();
      when(geocodeResponse.getResults()).thenReturn(results);
      try {
          underTest.latLong(address);
          fail("BusinessException expected");
      } catch (BusinessException e) {
          assertEquals("geocode.no.results", e.getMessageCode());
      }
      verify(address).getFormattedAddress();
      verify(geocoder, times(11)).geocode(Matchers.<GeocoderRequest>any());
      verifyNoMoreInteractions(address, geocoder);
  }

  @Test
  public void latLongShouldThrowBusinessExceptionWithTooManyResults() throws Exception {

      Address address = mock(Address.class);
      String formattedAddress = "15 rue La Fayette, 75009 Paris, France";
      when(address.getFormattedAddress()).thenReturn(formattedAddress);
      GeocodeResponse geocodeResponse = mock(GeocodeResponse.class);
      when(geocoder.geocode(Matchers.<GeocoderRequest>any())).thenReturn(geocodeResponse);
      List<GeocoderResult> results = Lists.newArrayList(mock(GeocoderResult.class), mock(GeocoderResult.class));
      when(geocodeResponse.getResults()).thenReturn(results);
      try {
          underTest.latLong(address);
          fail("BusinessException expected");
      } catch (BusinessException e) {
          assertEquals("geocode.too.many.results", e.getMessageCode());
      }
      verify(address).getFormattedAddress();
      verify(geocoder, times(2)).geocode(Matchers.<GeocoderRequest>any());
      verifyNoMoreInteractions(address, geocoder);
  }

  @Test
  public void latLongShouldNotGeocodeWithNullAddress() throws Exception {
    Address address = null;
    underTest.latLong(address);
    verifyZeroInteractions(geocoder);
  }

  @Test
  public void latLongShouldNotGeocodeWithNullFormattedAddress() throws Exception {
    Address address = mock(Address.class);
    underTest.latLong(address);
    verifyZeroInteractions(geocoder);
  }

  @Test
  public void latLongShouldNotGeocodeWithEmptyFormattedAddress() throws Exception {
    Address address = mock(Address.class);
    when(address.getFormattedAddress()).thenReturn("");
    underTest.latLong(address);
    verifyZeroInteractions(geocoder);
  }
  
  @Test
  public void latLongShouldSucceed() throws Exception {

      Address address = mock(Address.class);
      String formattedAddress = "15 rue La Fayette, 75009 Paris, France";
      when(address.getFormattedAddress()).thenReturn(formattedAddress);
      GeocodeResponse geocodeResponse = mock(GeocodeResponse.class);
      when(geocoder.geocode(Matchers.<GeocoderRequest>any())).thenReturn(geocodeResponse);
      final GeocoderResult result = mock(GeocoderResult.class);
      List<GeocoderResult> results = Lists.newArrayList(result);
      when(geocodeResponse.getResults()).thenReturn(results);
      GeocoderGeometry geometry = mock(GeocoderGeometry.class);
      when(result.getGeometry()).thenReturn(geometry);
      LatLng location = mock(LatLng.class);
      when(geometry.getLocation()).thenReturn(location);
      BigDecimal lat = mock(BigDecimal.class);
      Double latAsDouble = 9d;
      when(lat.doubleValue()).thenReturn(latAsDouble);
      when(location.getLat()).thenReturn(lat);
      BigDecimal lng = mock(BigDecimal.class);
      Double lngAsDouble = 5d;
      when(lng.doubleValue()).thenReturn(lngAsDouble);
      when(location.getLng()).thenReturn(lng);
      underTest.latLong(address);
      verify(address).getFormattedAddress();
      verify(address).addCoordinates(latAsDouble, lngAsDouble);
      verify(geocoder, times(2)).geocode(Matchers.<GeocoderRequest>any());
      verify(geocodeResponse).getResults();
      verify(result).getGeometry();
      verify(geometry).getLocation();
      verify(location).getLat();
      verify(lat).doubleValue();
      verify(lng).doubleValue();
      verify(location).getLng();
      verifyNoMoreInteractions(address, geocodeResponse, geocoder, result, geometry, location);

  }

}
