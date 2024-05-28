package com.dmdev.cache;

import com.dmdev.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

public class secondLevelCache {

    SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    @Test
    void entity_is_fetched_for_the_first_time() {
        prepareData();

        var session = sessionFactory.openSession();

        DepartmentEntity department = (DepartmentEntity) session.load(DepartmentEntity.class, 1L);
        System.out.println(department.getName());

        System.out.println(sessionFactory.getStatistics().getEntityFetchCount());           //Prints 1
        System.out.println(sessionFactory.getStatistics().getSecondLevelCacheHitCount());   //Prints 0
    }

    private void prepareData() {
        var session = sessionFactory.openSession();
        session.beginTransaction();

        var departmentEntity = new DepartmentEntity(1L, "SomeName");
        session.save(departmentEntity);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
}
