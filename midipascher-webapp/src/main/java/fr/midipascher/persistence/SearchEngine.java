package fr.midipascher.persistence;

import fr.midipascher.domain.Restaurant;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 14/09/12
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public interface SearchEngine {

    String INDEX_NAME = SearchIndices.midipascher.toString();
    String TYPE_NAME = SearchTypes.restaurant.toString();

    List<Restaurant> findRestaurantsByCriteria(Restaurant criteria);

}
