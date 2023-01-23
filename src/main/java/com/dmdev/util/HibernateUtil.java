package com.dmdev.util;

import com.dmdev.converter.BirthdayConverter;
import com.dmdev.entity.Audit;
import com.dmdev.entity.User;
import com.dmdev.linterceptor.GlobalInterceptor;
import com.dmdev.listeners.AuditTableListeners;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        registerRegister(sessionFactory);
        return sessionFactory;
    }

    private static void registerRegister(SessionFactory sessionFactory) {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry listenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        AuditTableListeners auditTableListeners = new AuditTableListeners();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListeners);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListeners);
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Audit.class);
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.setInterceptor(new GlobalInterceptor());
        configuration.registerTypeOverride(new JsonBinaryType());
        return configuration;
    }
}
