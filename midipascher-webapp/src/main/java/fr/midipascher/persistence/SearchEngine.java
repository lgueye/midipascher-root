package fr.midipascher.persistence;

import fr.midipascher.domain.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 14/09/12
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public interface SearchEngine {

    List<Restaurant> findRestaurantsByCriteria(Restaurant criteria);

}
