/**
 *
 */
package fr.midipascher.persistence.impl;

import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class HibernateListenersConfigurer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PreInsertEventListener preInsertEventListener;

    @Autowired
    private PreUpdateEventListener preUpdateEventListener;

    @Autowired
    private PreDeleteEventListener preDeleteEventListener;

    @PostConstruct
    public void registerListeners() {

        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;

        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();

        EventListenerRegistry registry = sessionFactoryImpl.getServiceRegistry()
                .getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(this.preInsertEventListener);

        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(this.preUpdateEventListener);

        registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(this.preDeleteEventListener);

    }

}
