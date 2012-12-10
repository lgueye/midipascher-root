package fr.midipascher.persistence.store;

import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.common.base.Strings;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.exceptions.BusinessException;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.HttpClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.core.MediaType;

/**
 * @author louis.gueye@gmail.com
 */
@Component(Geocoder.BEAN_ID)
public class Geocoder {

    public static final String BEAN_ID = "geocoder";

    @Autowired
    private com.google.code.geocoder.Geocoder googleGeocoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(Geocoder.class);
    private Client jerseyClient = null;

    public Geocoder() {
      final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
      this.jerseyClient = ApacheHttpClient4.create(config);
      this.jerseyClient.addFilter(new LoggingFilter());
      config.getClasses().add(JacksonJsonProvider.class);
      config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    }

    @Async
    public void latLong(final Address address) throws UnsupportedEncodingException {
      if (address == null || Strings.isNullOrEmpty(address.formattedAddress())) return;
      GeocodeResponse geocoderResponse = responseFromAddress(address);
      ClientResponse response;

        final String formattedAddress = address.formattedAddress();
        //GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(formattedAddress).setLanguage("en").getGeocoderRequest();
        //GeocodeResponse geocoderResponse = googleGeocoder.geocode(geocoderRequest);
        int counTrials = 0;
        while (geocoderResponse == null && counTrials < 10) {
          geocoderResponse = responseFromAddress(address);
          counTrials++;
        }
        if (geocoderResponse == null) {
          String message = "Could not contact the geocoder service";
          LOGGER.error(message);
          throw new BusinessException("geocode.service.unavailable");
        }

        List<GeocoderResult> results = geocoderResponse.getResults();
        if (CollectionUtils.isEmpty(results)) {
            String message = "The geocoding service found no match for address [{}]";
            LOGGER.error(message,  formattedAddress);
            throw new BusinessException("geocode.no.results", new Object[]{formattedAddress}, message);
        }
        int countResults = results.size();
        if (countResults > 1) {
            String message = "The geocoding service found {} matches for addresses [{}]";
            LOGGER.error(message, countResults, formattedAddress);
            throw new BusinessException("geocode.too.many.results", new Object[]{countResults, formattedAddress}, message);
        }

        GeocoderResult result = results.iterator().next();
        LatLng location = result.getGeometry().getLocation();
        BigDecimal lat = location.getLat();
        BigDecimal lng = location.getLng();
        address.setLatitude(lat);
        address.setLongitude(lng);
    }

  private GeocodeResponse responseFromAddress(Address address) throws UnsupportedEncodingException {
    final String encodedAddress = new URLCodec().encode(address.formattedAddress(), "UTF-8");
    ClientResponse response = jerseyClient.resource(
        "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=" + encodedAddress
        + "&language=en").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    return response.getEntity(GeocodeResponse.class);
  }
}
