package fr.midipascher.persistence.store;

import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.exceptions.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
@Component(Geocoder.BEAN_ID)
public class Geocoder {

    public static final String BEAN_ID = "geocoder";

    @Autowired
    private com.google.code.geocoder.Geocoder googleGeocoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(Geocoder.class);

    @Async
    public void latLong(final Address address) {
      final String formattedAddress = address.formattedAddress();
      GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(formattedAddress).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = googleGeocoder.geocode(geocoderRequest);
        List<GeocoderResult> results = geocoderResponse.getResults();
        if (CollectionUtils.isEmpty(results)) {
            String message = "Please provide a precise address, geocoding failed for " + formattedAddress;
            LOGGER.error(message);
            throw new BusinessException("geocode.no.results", null, message);
        }
        int countResults = results.size();
        if (countResults > 1) {
            String message = "Please provide a precise address, geocoding found " + countResults + " addresses for " + formattedAddress;
            LOGGER.error(message);
            throw new BusinessException("geocode.too.many.results", new Object[]{countResults}, message);
        }

        GeocoderResult result = results.iterator().next();
        LatLng location = result.getGeometry().getLocation();
        BigDecimal lat = location.getLat();
        BigDecimal lng = location.getLng();
        address.setLatitude(lat);
        address.setLongitude(lng);
    }
}
