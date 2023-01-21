package com.dmdev;

import com.dmdev.entity.Company;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
//        Company company = Company.builder()
//                .name("Amazon")
//                .build();
//        User user = null;


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
//                TestDataImporter.importData(sessionFactory);
                Transaction transaction = session1.beginTransaction();

//                session1.save(user);
                List<User> users = session1.createQuery("select u from User u where 1=1", User.class)
                        .list();

                users.forEach(user -> System.out.println(user.getPayments().size()));
                users.forEach(user -> System.out.println(user.getCompany().getName()));

                session1.getTransaction().commit();
            }
        }
    }












}
