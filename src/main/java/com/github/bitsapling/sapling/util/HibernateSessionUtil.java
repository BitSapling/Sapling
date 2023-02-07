package com.github.bitsapling.sapling.util;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class HibernateSessionUtil {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public SessionFactory getSessionFactory(){
        return entityManagerFactory.unwrap(SessionFactory.class);
    }

    public void closeFromThread(boolean participate) {
        if (!participate) {
            SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
                    .unbindResource(getSessionFactory());
            SessionFactoryUtils.closeSession(sessionHolder.getSession());
        }
    }

    public boolean bindToThread() {
        SessionFactory sessionFactory = getSessionFactory();
        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            // Do not modify the Session: just set the participate flag.
            return true;
        } else {
            Session session = sessionFactory.openSession();
            session.setFlushMode(FlushMode.MANUAL.toJpaFlushMode());
            SessionHolder sessionHolder = new SessionHolder(session);
            TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        }
        return false;
    }
}
