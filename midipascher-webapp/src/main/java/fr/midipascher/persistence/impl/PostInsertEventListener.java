/**
 *
 */
package fr.midipascher.persistence.impl;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.validation.ValidationContext;
import fr.midipascher.persistence.SearchEngine;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PostInsertEventListener.BEAN_ID)
public class PostInsertEventListener implements
        org.hibernate.event.spi.PostInsertEventListener {

    public static final String BEAN_ID = "postInsertEventListener";

    @Autowired
    private SearchEngine searchEngine;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        searchEngine.index( (AbstractEntity)event.getEntity());
    }
}
