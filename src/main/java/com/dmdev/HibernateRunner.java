package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ReplicationMode;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
public class HibernateRunner {


    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                Payment payment = session.find(Payment
                        .class, 1L);

                payment.setAmount(payment.getAmount() + 1);
                session.save(payment);

                session.getTransaction().commit();
            }


            try (var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                AuditReader auditReader = AuditReaderFactory.get(session2);
                Payment oldPayment = auditReader.find(Payment.class, 1L, 1L);
//                Payment oldPayment = auditReader.find(Payment.class, 1L, new Date(1674535185209L));
//                session2.replicate(oldPayment, ReplicationMode.OVERWRITE);

                List resultList = auditReader.createQuery()
                        .forEntitiesAtRevision(Payment.class, 400L)
                        .add(AuditEntity.property("amount").ge(450))
                        .add(AuditEntity.property("id").ge(6L)
                        )
                        .addProjection(AuditEntity.property("amount"))
                        .getResultList();


                session2.getTransaction().commit();
            }

        }
    }

    ;
}
/*/


/**
 * 1. Avoid @OneOnOne bidirectional (если ключ натуральный можно у зависимой(fetch.Lazy+ optinal.false))
 * (как бы не оптимизировали все равно будем его получать)
 * 2. Use fetch type LAZY everywhere
 * 3.Don't prefer @BatchSize, @Fetch
 * 4. Use query fetch (Hql, Criteria Api , QueryDsl)( но он не работает в query by id)
 * 5. Prefer EntityGraph Api than @FetchProfile
 * <p>
 * <p>
 * ! преждевременное улушчшение производительность ухуджает
 * скорость разработки приложения и убиать суть hibernate
 * быстрая разработка приложения и работа с бд
 * !Сначала бизнес логика потом оптимизация
 */

/*
 *  save + why deprecate + когда делается запрос
 *  saveOrUpdate  + why deprecate + когда делается запрос
 *  delete + когда делается запрос
 *  update + когда делается запрос
 *  persistenceContext , entitysByKey
 * Способы очистить  persistance context
 *  evict
 *  clear
 *  Что будет если изменить сущность в процессе сессии? dirtiesContext
 *  В рамках сессии изменяем сущ уоторая ассоциирована с сессией и
 * делаем ее dirty(и это отразится в sql запросе)
 *
 * Состояния сущностей Transient,Persistent,Removed, Detached
 *
 * Почему лучще использовать синтетические ключи вмемто естественых(производительность легче менять логику)
 *
    @GeneratedValue(strategy = GenerationType.AUTO) + какая для постгре
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE) + СВОЙ СИКВЕНС
 *
 *
 *@Access(AccessType.PROPERTY)
  *@Access(AccessType.FIELD)
 *@Transient
 * @Temporal
 * @ColumnTransformer  @Formula
 *
    @ManyToOne(optional = false)
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
@ToString(exclude = "company")
* Proxy.newProxyInstance
*
* Hibernate для создания прокси ByteBuddy - использует сейчас
* Hibernate.unproxy()
    *
    *
 *
 *Зачем делать @EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users") если в OneToMany стоит Set<>
 *
 *
 * Что использовать для equals hashcode в entity(уникальное поле для entity натуральный ключ)
 *если такого поля нет, то все поля без маппинга
 *
 * @PrimaryKeyJoinColumn
 *
 *
 * Почему list or collection лучше чем set- потому что есть PersistentBag + проблемы с equal hashcode
 *
 *@ElementCollection
 *
 *Почему не стоит использовать  @OrderColumn:  используется только с листами, колонка именно int типа,
 * должен быть четкий порядок и не должно быть пропусков (пропуски заполнятся null)
 *(такой вариант хорош если есть справочник который легко ложится на енам )
 *
 *
 *@SortNatural and TreeSet
 *
 * @SortComparator
 *  @MapKey     @MapKeyEnumerated     @MapsId(чтобы связать с embededid)
 * @MapKeyColumn

 *
 * ограничения Fetch , fetch join -  могут использоваться  только с коллекциец
 бесполезны если маппинг по одной сущности
 *
 *
 * @FetchProfile - для работы с одной сущщностью
 *
 *
 * for update, for share
 *
 * autoCommitMode
 */


