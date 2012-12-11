package fr.midipascher.persistence.store;

import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.common.base.Strings;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.exceptions.BusinessException;

/**
 * @author louis.gueye@gmail.com
 */
@Component(Geocoder.BEAN_ID)
public class Geocoder {

    public static final String BEAN_ID = "geocoder";

    @Autowired
    private com.google.code.geocoder.Geocoder googleGeocoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(Geocoder.class);

    public void latLong(final Address address) throws UnsupportedEncodingException {
        if (address == null) return;
        String formattedAddress = address.getFormattedAddress();
        LatLng location = latLong(formattedAddress);
        if (location != null) {
            BigDecimal lat = location.getLat();
            BigDecimal lng = location.getLng();
            address.addCoordinates(lat, lng);
        }
    }

    @Async
    private LatLng latLong(final String address) throws UnsupportedEncodingException {

        if (Strings.isNullOrEmpty(address)) return null;
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = googleGeocoder.geocode(geocoderRequest);
        List<GeocoderResult> results = null;
        int counTrials = 0;
        boolean shouldRetry = true;
        while (shouldRetry) {
            geocoderResponse = googleGeocoder.geocode(geocoderRequest);
            results = (geocoderResponse == null ? null : geocoderResponse.getResults());
            counTrials++;
            shouldRetry = (geocoderResponse == null || CollectionUtils.isEmpty(results)) && counTrials < 10;
        }
        if (geocoderResponse == null) {
            String message = "Could not contact the geocoder service";
            LOGGER.error(message);
            throw new BusinessException("geocode.service.unavailable");
        }

        if (CollectionUtils.isEmpty(results)) {
            String message = "The geocoding service found no match for address [{}]";
            LOGGER.error(message, address);
            throw new BusinessException("geocode.no.results", new Object[]{address}, message);
        }
        int countResults = results.size();
        if (countResults > 1) {
            String message = "The geocoding service found {} matches for addresses [{}]";
            LOGGER.error(message, countResults, address);
            throw new BusinessException("geocode.too.many.results", new Object[]{countResults, address}, message);
        }

        GeocoderResult result = results.iterator().next();
        return result.getGeometry().getLocation();
    }

}
