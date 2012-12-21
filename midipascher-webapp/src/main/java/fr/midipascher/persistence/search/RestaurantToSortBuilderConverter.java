package fr.midipascher.persistence.search;

import com.google.code.geocoder.model.LatLng;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.Coordinates;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.store.Geocoder;

/**
 * @author louis.gueye@gmail.com
 */
@Component(RestaurantToSortBuilderConverter.BEAN_ID)
public class RestaurantToSortBuilderConverter implements Converter<Restaurant, List<SortBuilder>> {

    public static final String BEAN_ID = "RestaurantToSortBuilderConverter";
    private static final
    Logger
        LOGGER =
        LoggerFactory.getLogger(RestaurantToSortBuilderConverter.class);

  @Autowired
    private Geocoder googleGeocoder;

    public List<SortBuilder> convert(Restaurant source) {
        Map<String, Object> criteria = criteriaAsMap(source);
        List<SortBuilder> sortBuilders = Lists.newArrayList();
        if (noCriteria(criteria)) {
            sortBuilders.add(SortBuilders.fieldSort("created").order(SortOrder.DESC));
            sortBuilders.add(SortBuilders.fieldSort("updated").order(SortOrder.DESC));
        } else {
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                sortBuilders.add(resolveQueryBuilder(field, value));
            }
        }
        return sortBuilders;
    }

    protected Map<String, Object> criteriaAsMap(Restaurant source) {

        if (source == null) return null;

        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        Address address = source.getAddress();
        if (address != null) {
            String formattedAddress = address.getFormattedAddress();
            if (!Strings.isNullOrEmpty(formattedAddress)) {
                try {
                    googleGeocoder.latLong(address);
                    Coordinates coordinates = address.getCoordinates();
                    if (coordinates != null) {
                      builder.put(RestaurantSortFieldsRegistry.LOCATION, coordinates);
                  }
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("Could not geocode address {}", address);
                }
            }
        }

        return builder.build();

    }

    protected SortBuilder resolveQueryBuilder(String field, Object value) {

        if (RestaurantSortFieldsRegistry.LOCATION.equals(field)) {
            Coordinates coordinates = (Coordinates)value;
            return SortBuilders
                    .geoDistanceSort("coordinates")
                    .point(coordinates.getLng().doubleValue(), coordinates.getLat().doubleValue())
                    .unit(DistanceUnit.KILOMETERS)
                    .order(SortOrder.ASC);
        }

        throw new UnsupportedOperationException("No sort builder resolved for field '" + field + "'");

    }

    protected boolean noCriteria(Map<String, Object> criteria) {

        return criteria == null || criteria.size() == 0;

    }
}
