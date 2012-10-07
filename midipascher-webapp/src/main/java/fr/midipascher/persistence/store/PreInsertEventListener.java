/**
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.validation.ValidationContext;
import org.hibernate.event.spi.PreInsertEvent;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PreInsertEventListener.BEAN_ID)
public class PreInsertEventListener extends AbstractEventListener implements
        org.hibernate.event.spi.PreInsertEventListener {

    public static final String BEAN_ID = "preInsertEventListener";

    /**
     *
     */
    private static final long serialVersionUID = 2153376355687873385L;

    /**
     * @see org.hibernate.event.spi.PreInsertEventListener#onPreInsert(org.hibernate.event.spi.PreInsertEvent)
     * @param event
     * @return
     */
    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        validate((AbstractEntity) event.getEntity(), ValidationContext.CREATE);
        return false;
    }

}