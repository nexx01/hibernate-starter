package org.example;

import org.example.firstEntity.SomeEntityWithUUIDGenerator;
import org.example.util.H2JDBCUtils;
import org.example.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {


   public static void main(String[] args) {
       var someEntityWithUUIDGenerator = new SomeEntityWithUUIDGenerator();
       someEntityWithUUIDGenerator.setBar("bar");
       someEntityWithUUIDGenerator.setFoo("foo");
       System.out.println(someEntityWithUUIDGenerator);

       var sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();

       var session = sessionFactory.openSession();
       session.beginTransaction();
       session.persist(someEntityWithUUIDGenerator);
       System.out.println(someEntityWithUUIDGenerator);
       session.flush();
       session.getTransaction().commit();
       session.close();

   }
}