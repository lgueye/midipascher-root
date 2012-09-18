/**
 *
 */
package fr.midipascher.persistence.impl;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.persistence.SearchEngine;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostUpdate;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PostUpdateEventListener.BEAN_ID)
public class PostUpdateEventListener implements
        org.hibernate.event.spi.PostUpdateEventListener {

    public static final String BEAN_ID = "postUpdateEventListener";

    @Autowired
    private SearchEngine searchEngine;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        searchEngine.index( (AbstractEntity)event.getEntity());
    }
}
