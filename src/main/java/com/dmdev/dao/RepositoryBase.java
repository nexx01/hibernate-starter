package com.dmdev.dao;

import com.dmdev.entity.BaseEntity;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable,E extends BaseEntity<K>> implements Repository<K,E> {
    private final Class<E> clazz;
//    private final SessionFactory sessionFactory;
    private final SessionFactory sessionFactory;

    @Override
    public E save(E entity) {
          var session = sessionFactory.getCurrentSession();

        session.save(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
          var session = sessionFactory.getCurrentSession();
        session.delete(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
          var session = sessionFactory.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> finById(K id) {
          var session = sessionFactory.getCurrentSession();

        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
          var session = sessionFactory.getCurrentSession();


        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<E> criteria = cb.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria)
                .getResultList();



//        return session.createQuery("select p from E p", clazz)
//                .getResultList();
    }
}
