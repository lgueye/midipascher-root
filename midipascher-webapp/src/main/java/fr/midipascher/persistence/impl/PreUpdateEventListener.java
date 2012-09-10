/**
 * 
 */
package fr.midipascher.persistence.impl;

import org.hibernate.event.spi.PreUpdateEvent;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.validation.ValidationContext;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PreUpdateEventListener.BEAN_ID)
public class PreUpdateEventListener extends AbstractEventListener implements
		org.hibernate.event.spi.PreUpdateEventListener {

	public static final String	BEAN_ID				= "preUpdateEventListener";

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2153376355687873385L;

	/**
	 * @see org.hibernate.event.PreUpdateEventListener#onPreUpdate(org.hibernate.event.PreUpdateEvent)
	 */
	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		validate((AbstractEntity) event.getEntity(), ValidationContext.UPDATE);
		return false;
	}

}
