package com.dmdev.cache;

import com.dmdev.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class firstLevelCache {

    SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @Test
    void fetch_entity_from_first_level() {
        prepareData();
        var session = sessionFactory.openSession();
        session.beginTransaction();

        var load = (DepartmentEntity) session.load(DepartmentEntity.class, 1L);
        System.out.println(load.getName());

        load = (DepartmentEntity) session.load(DepartmentEntity.class, 1L);
        System.out.println(load.getName());

        session.getTransaction().commit();
    }

    @Test
    void retrieve_entity_in_different_session() {
        prepareData();

        var session = sessionFactory.openSession();
        session.beginTransaction();
        var session1 = sessionFactory.openSession();
        session1.beginTransaction();

        try{
            var department = session.load(DepartmentEntity.class, 1L);
            System.out.println(department.getName());

            department = session.load(DepartmentEntity.class, 1L);
            System.out.println(department.getName());

            var department1 = session1.load(DepartmentEntity.class, 1L);
            System.out.println(department1.getName());

        } finally {
            session.getTransaction().commit();
            session1.getTransaction().commit();
        }
    }

    @Test
    void removing_cached_entity_from_first_level_cache() {
        prepareData();

        var session = sessionFactory.openSession();
        session.beginTransaction();

        try{
            var department = session.load(DepartmentEntity.class, 1L);
            System.out.println(department.getName());

            department = session.load(DepartmentEntity.class, 1L);
            System.out.println(department.getName());

            session.evict(department);
//            session.clear();

            department = session.load(DepartmentEntity.class, 1L);
            System.out.println(department.getName());
        } finally {
            session.getTransaction().commit();
        }
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
