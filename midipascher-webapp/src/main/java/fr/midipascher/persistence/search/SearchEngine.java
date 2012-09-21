package fr.midipascher.persistence.search;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.Restaurant;

import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public interface SearchEngine {

    String INDEX_NAME = SearchIndices.midipascher.toString();

    String RESTAURANT_TYPE_NAME = SearchTypes.restaurant.toString();

    List<Restaurant> findRestaurantsByCriteria(Restaurant criteria);

    void index(AbstractEntity entity);

    void removeFromIndex(AbstractEntity entity);
}
