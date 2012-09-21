/**
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.validation.ValidationContext;
import org.hibernate.event.spi.PreDeleteEvent;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PreDeleteEventListener.BEAN_ID)
public class PreDeleteEventListener extends AbstractEventListener implements
        org.hibernate.event.spi.PreDeleteEventListener {

    public static final String BEAN_ID = "preDeleteEventListener";

    /**
     *
     */
    private static final long serialVersionUID = 2153376355687873385L;

    /**
     * @see org.hibernate.event.PreDeleteEventListener#onPreDelete(org.hibernate.event.PreDeleteEvent)
     */
    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        validate((AbstractEntity) event.getEntity(), ValidationContext.DELETE);
        return false;
    }

}
