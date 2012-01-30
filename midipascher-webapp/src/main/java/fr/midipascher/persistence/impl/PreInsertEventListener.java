/**
 * 
 */
package fr.midipascher.persistence.impl;

import org.hibernate.event.PreInsertEvent;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PreInsertEventListener.BEAN_ID)
public class PreInsertEventListener implements org.hibernate.event.PreInsertEventListener {

	public static final String	BEAN_ID				= "preInsertEventListener";

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2153376355687873385L;

	/**
	 * @see org.hibernate.event.PreInsertEventListener#onPreInsert(org.hibernate.event.PreInsertEvent)
	 */
	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
