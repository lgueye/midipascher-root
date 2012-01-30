/**
 * 
 */
package fr.midipascher.persistence.impl;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;

/**
 * @author louis.gueye@gmail.com
 */
public class HibernateExtendedPersistenceProvider extends HibernatePersistence {

	private PreInsertEventListener[]	preInsertEventListeners;
	private PreUpdateEventListener[]	preUpdateEventListeners;
	private PreDeleteEventListener[]	preDeleteEventListeners;

	@SuppressWarnings("rawtypes")
	@Override
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		setupConfiguration(cfg);
		Ejb3Configuration configured = cfg.configure(persistenceUnitName, properties);
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		setupConfiguration(cfg);
		Ejb3Configuration configured = cfg.configure(info, properties);
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	private void setupConfiguration(Ejb3Configuration cfg) {
		cfg.getEventListeners().setPreInsertEventListeners(this.preInsertEventListeners);
		cfg.getEventListeners().setPreDeleteEventListeners(this.preDeleteEventListeners);
		cfg.getEventListeners().setPreUpdateEventListeners(this.preUpdateEventListeners);
	}

	/**
	 * @param preInsertEventListeners
	 *            the preInsertEventListeners to set
	 */
	public void setPreInsertEventListeners(PreInsertEventListener[] preInsertEventListeners) {
		this.preInsertEventListeners = preInsertEventListeners;
	}

	/**
	 * @param preUpdateEventListeners
	 *            the preUpdateEventListeners to set
	 */
	public void setPreUpdateEventListeners(PreUpdateEventListener[] preUpdateEventListeners) {
		this.preUpdateEventListeners = preUpdateEventListeners;
	}

	/**
	 * @param preDeleteEventListeners
	 *            the preDeleteEventListeners to set
	 */
	public void setPreDeleteEventListeners(PreDeleteEventListener[] preDeleteEventListeners) {
		this.preDeleteEventListeners = preDeleteEventListeners;
	}

}
