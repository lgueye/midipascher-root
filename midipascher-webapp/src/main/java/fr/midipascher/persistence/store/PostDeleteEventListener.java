/**
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.persistence.search.SearchEngine;

import org.hibernate.event.spi.PostDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PostDeleteEventListener.BEAN_ID)
public class PostDeleteEventListener implements
        org.hibernate.event.spi.PostDeleteEventListener {

    public static final String BEAN_ID = "postDeleteEventListener";

    @Autowired
    private SearchEngine searchEngine;

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        searchEngine.removeFromIndex( (AbstractEntity)event.getEntity());
    }
}
