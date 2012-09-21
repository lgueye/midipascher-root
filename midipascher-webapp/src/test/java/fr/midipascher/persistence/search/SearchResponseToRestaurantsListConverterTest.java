package fr.midipascher.persistence.search;

import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.JsonByteArrayToRestaurantConverter;
import fr.midipascher.persistence.search.SearchResponseToRestaurantsListConverter;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.InternalSearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: lgueye Date: 17/09/12 Time: 18:05
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchResponseToRestaurantsListConverterTest {

    @Mock
    private JsonByteArrayToRestaurantConverter byteArrayToRestaurantConverter;

    @InjectMocks
    private SearchResponseToRestaurantsListConverter underTest = new SearchResponseToRestaurantsListConverter();

    @Test
    public void convertShouldSucceed() throws Exception {

        SearchResponse searchResponse = mock(SearchResponse.class);

        InternalSearchHit searchHit = mock(InternalSearchHit.class);
        SearchHits searchHits = new InternalSearchHits(new InternalSearchHit[]{searchHit}, 45, 4.2f);
        when(searchResponse.getHits()).thenReturn(searchHits);
        byte[] restaurantAsBytes = "{}".getBytes();
        when(searchHit.source()).thenReturn(restaurantAsBytes);
        Restaurant restaurant = mock(Restaurant.class);
        when(byteArrayToRestaurantConverter.convert(restaurantAsBytes)).thenReturn(restaurant);
        List<Restaurant> restaurants = underTest.convert(searchResponse);
        assertSame(restaurant, restaurants.get(0));
    }

}
