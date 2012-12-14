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
import org.hibernate.event.spi.PreUpdateEvent;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PreUpdateEventListener.BEAN_ID)
public class PreUpdateEventListener implements
        org.hibernate.event.spi.PreUpdateEventListener {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(PreUpdateEventListener.class);

    @Autowired
    private PreModifyValidator preModifyValidator;

    public static final String BEAN_ID = "preUpdateEventListener";

    @Autowired
    private Geocoder geocoder;

    /**
     *
     */
    private static final long serialVersionUID = 2153376355687873385L;

    /**
     * @see org.hibernate.event.spi.PreUpdateEventListener#onPreUpdate(org.hibernate.event.spi.PreUpdateEvent)
     */
    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        final Object eventEntity = event.getEntity();
        preModifyValidator.validate((AbstractEntity) eventEntity, ValidationContext.UPDATE);
        if (eventEntity instanceof EventAware) {
            ((EventAware)eventEntity).setUpdated(new DateTime());
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
