package fr.midipascher.persistence.search;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.Restaurant;

/**
 * @author louis.gueye@gmail.com
 */
@Repository
public class SearchEngineImpl implements SearchEngine {

    @Autowired
    private Client elasticsearch;

    @Autowired
    private RestaurantToQueryBuilderConverter restaurantToQueryBuilderConverter;

    @Autowired
    private SearchResponseToRestaurantsListConverter searchResponseToRestaurantsListConverter;

    @Autowired
    private RestaurantToJsonByteArrayConverter restaurantToByteArrayConverter;

    @Autowired
    private RestaurantToSortBuilderConverter restaurantToSortBuilderConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngineImpl.class);

    @Override
    public List<Restaurant> findRestaurantsByCriteria(Restaurant criteria) {
        QueryBuilder queryBuilder = restaurantToQueryBuilderConverter.convert(criteria);
        List<SortBuilder> sortBuilders = restaurantToSortBuilderConverter.convert(criteria);
        SearchRequestBuilder searchRequestBuilder = elasticsearch
                    .prepareSearch(INDEX_NAME)
                    .setTypes(RESTAURANT_TYPE_NAME)
                    .setQuery(queryBuilder);
        for (SortBuilder sortBuilder : sortBuilders) {
            searchRequestBuilder.addSort(sortBuilder);
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return searchResponseToRestaurantsListConverter.convert(searchResponse);
    }

    @Override
    public void index(AbstractEntity entity) {
        if (entity instanceof Restaurant) {
            byte[] restaurantAsBytes = restaurantToByteArrayConverter.convert((Restaurant) entity);
            elasticsearch.prepareIndex(INDEX_NAME, RESTAURANT_TYPE_NAME).setId(entity.getId().toString()).setSource(restaurantAsBytes).setRefresh(true).execute().actionGet();
        }
    }

    @Override
    public void removeFromIndex(AbstractEntity entity) {
        if (entity instanceof Restaurant) {
            elasticsearch.prepareDelete(INDEX_NAME, RESTAURANT_TYPE_NAME, entity.getId().toString()).setRefresh(true).execute().actionGet();
        }
    }
}
