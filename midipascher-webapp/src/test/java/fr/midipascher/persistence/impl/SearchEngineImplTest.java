package fr.midipascher.persistence.impl;

import com.google.common.collect.Lists;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.SearchEngine;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
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
 * User: lgueye Date: 17/09/12 Time: 17:44
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchEngineImplTest {

    @Mock
    Client elasticsearch;

    @Mock
    private RestaurantToQueryBuilderConverter restaurantToQueryBuilderConverter;

    @Mock
    private SearchResponseToRestaurantsListConverter searchResponseToRestaurantsListConverter;

    @InjectMocks
    private SearchEngine underTest = new SearchEngineImpl();

    @Test
    public void findRestaurantsByCriteriaShouldSucceed() throws Exception {

        Restaurant criteria = mock(Restaurant.class);
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        when(restaurantToQueryBuilderConverter.convert(criteria)).thenReturn(queryBuilder);
        SearchRequestBuilder searchRequestBuilder = mock(SearchRequestBuilder.class);
        when(elasticsearch.prepareSearch(SearchEngine.INDEX_NAME)).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setTypes(SearchEngine.TYPE_NAME)).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setQuery(queryBuilder)).thenReturn(searchRequestBuilder);
        ListenableActionFuture<SearchResponse> actionFuture = mock(ListenableActionFuture.class);
        when(searchRequestBuilder.execute()).thenReturn(actionFuture);
        SearchResponse searchResponse = mock(SearchResponse.class);
        when(actionFuture.actionGet()).thenReturn(searchResponse);
        Restaurant r0 = mock(Restaurant.class);
        Restaurant r1 = mock(Restaurant.class);
        Restaurant r2 = mock(Restaurant.class);
        List<Restaurant> results = Lists.newArrayList(r0, r1, r2);
        when(searchResponseToRestaurantsListConverter.convert(searchResponse)).thenReturn(results);
        List<Restaurant> restaurants = underTest.findRestaurantsByCriteria(criteria);

        assertSame(restaurants, results);

    }
}
