/**
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.Coordinates;
import fr.midipascher.domain.EventAware;
import fr.midipascher.domain.LocationAware;
import fr.midipascher.domain.validation.ValidationContext;
import org.hibernate.event.spi.PreInsertEvent;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PreInsertEventListener.BEAN_ID)
public class PreInsertEventListener implements
        org.hibernate.event.spi.PreInsertEventListener {

    public static final String BEAN_ID = "preInsertEventListener";

    private static final Logger LOGGER =
        LoggerFactory.getLogger(PreInsertEventListener.class);

    @Autowired
    private PreModifyValidator preModifyValidator;

    /**
     *
     */
    private static final long serialVersionUID = 2153376355687873385L;

    @Autowired
    private Geocoder geocoder;

    /**
     * @param event
     * @return
     * @see org.hibernate.event.spi.PreInsertEventListener#onPreInsert(org.hibernate.event.spi.PreInsertEvent)
     */
    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        final Object eventEntity = event.getEntity();
        preModifyValidator.validate((AbstractEntity) eventEntity, ValidationContext.CREATE);
        if (eventEntity instanceof EventAware) {
            ((EventAware) eventEntity).setCreated(new DateTime());
        }
        if (eventEntity instanceof LocationAware) {
            Address address = null;
            try {
                address = ((LocationAware) eventEntity).getAddress();
                address.formattedAddress();
                geocoder.latLong(address);
            } catch (Throwable e) {
              // Insert/Update should never fail due to geocode issue
              LOGGER.warn("Could not geocode address [{}]", address);
            }
        }
        return false;
    }

}
