package fr.midipascher.persistence.impl;

import com.google.common.collect.Lists;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.JsonByteArrayToRestaurantConverter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 15/09/12
 * Time: 00:32
 * To change this template use File | Settings | File Templates.
 */
@Component(SearchResponseToRestaurantsListConverter.BEAN_ID)
public class SearchResponseToRestaurantsListConverter implements Converter<SearchResponse, List<Restaurant>> {

    public static final String BEAN_ID = "SearchResponseToRestaurantsListConverter";

    @Autowired
    private JsonByteArrayToRestaurantConverter byteArrayToRestaurantConverter;

    @Override
    public List<Restaurant> convert(SearchResponse source) {

        List<Restaurant> restaurants = Lists.newArrayList();

        if (source != null) {

            SearchHits searchHits = source.getHits();

            for (SearchHit searchHit : searchHits) {

                byte[] restaurantAsBytes = searchHit.source();

                Restaurant restaurant = byteArrayToRestaurantConverter.convert(restaurantAsBytes);

                restaurants.add(restaurant);
            }

        }

        return restaurants;

    }

}
