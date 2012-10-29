package fr.midipascher.domain;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: lgueye Date: 29/10/12 Time: 15:36
 */
public class AddressTest {

  private Address underTest;

  @Before
  public void before() throws Exception {
    underTest = new Address();
  }

  @Test
  public void formattedAddressShouldSucceed() throws Exception {
      underTest.setCity("paris");
      underTest.setCountryCode("fr");
      underTest.setPostalCode("75009");
      underTest.setStreetAddress("22 rue la fayette");
      String formattedAddress = underTest.formattedAddress();
      assertEquals("22 Rue La Fayette, 75009 Paris, France", formattedAddress);
//      final Geocoder geocoder = new Geocoder();
//      GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(formattedAddress).setLanguage("en").getGeocoderRequest();
//      GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
//      final List<GeocoderResult> results = geocoderResponse.getResults();
//      assertEquals(1, results.size());
//      assertEquals(formattedAddress, results.iterator().next().getFormattedAddress());
  }
  
}
