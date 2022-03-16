package ru.javawebinar.topjava.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Conditional;
import ru.javawebinar.topjava.util.IsNotJdbcProfile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Conditional(IsNotJdbcProfile.class)
public class JpaUtil {

    @PersistenceContext
    private EntityManager em;

    public void clear2ndLevelHibernateCache() {
        Session s = (Session) em.getDelegate();
        SessionFactory sf = s.getSessionFactory();
//        sf.getCache().evictEntityData(User.class, AbstractBaseEntity.START_SEQ);
//        sf.getCache().evictEntityData(User.class);
        sf.getCache().evictAllRegions();
    }
}
