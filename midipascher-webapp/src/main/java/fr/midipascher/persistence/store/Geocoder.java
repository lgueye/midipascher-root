package fr.midipascher.persistence.store;

import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.exceptions.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class Geocoder {

    @Autowired
    private com.google.code.geocoder.Geocoder googleGeocoder;

    public void latLong(final Address address) {
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address.formatAddress()).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = googleGeocoder.geocode(geocoderRequest);
        List<GeocoderResult> results = geocoderResponse.getResults();
        if (CollectionUtils.isEmpty(results))
            throw new BusinessException("geocode.no.results", null, "Please provide a precise address, geocoding failed for " + address.formatAddress());
        int countResults = results.size();
        if (countResults > 1)
            throw new BusinessException("geocode.too.many.results", new Object[]{countResults}, "Please provide a precise address, geocoding found " + countResults + " addresses for " + address.formatAddress());
        GeocoderResult result = results.iterator().next();
        LatLng location = result.getGeometry().getLocation();
        BigDecimal lat = location.getLat();
        BigDecimal lng = location.getLng();
        address.setLatitude(lat);
        address.setLongitude(lng);
    }
}
