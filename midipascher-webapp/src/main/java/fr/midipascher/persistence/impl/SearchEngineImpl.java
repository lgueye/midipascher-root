package fr.midipascher.persistence.impl;

import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.SearchEngine;
import fr.midipascher.persistence.SearchIndices;
import fr.midipascher.persistence.SearchTypes;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 14/09/12
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SearchEngineImpl implements SearchEngine {

    @Autowired
    private Client elasticsearch;

    @Autowired
    private RestaurantToQueryBuilderConverter restaurantToQueryBuilderConverter;

    @Autowired
    private SearchResponseToRestaurantsListConverter searchResponseToRestaurantsListConverter;

    @Override
    public List<Restaurant> findRestaurantsByCriteria(Restaurant criteria) {

        QueryBuilder queryBuilder = restaurantToQueryBuilderConverter.convert(criteria);

        SearchResponse searchResponse = elasticsearch.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(queryBuilder).execute().actionGet();

        List<Restaurant> results = searchResponseToRestaurantsListConverter.convert(searchResponse);

        return results;

    }
}
