/**
 * 
 */
package fr.midipascher.persistence.impl;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author louis.gueye@gmail.com
 */
// @Component
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

        final HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) entityManagerFactory;

        final SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory
                .getSessionFactory();

        sessionFactoryImpl.getEventListeners().setPreInsertEventListeners(
            new org.hibernate.event.PreInsertEventListener[] { preInsertEventListener });
        sessionFactoryImpl.getEventListeners().setPreUpdateEventListeners(
            new org.hibernate.event.PreUpdateEventListener[] { preUpdateEventListener });
        sessionFactoryImpl.getEventListeners().setPreDeleteEventListeners(
            new org.hibernate.event.PreDeleteEventListener[] { preDeleteEventListener });
    }

}
