package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.entity.Profile;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class HibernateRunner {


//    @Transactional
    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session1 = sessionFactory.openSession();
             Session session2 = sessionFactory.openSession()) {
            TestDataImporter.importData(sessionFactory);
            session1.beginTransaction();
            session1.createQuery("select p from Payment  p")
                    .list();

            Payment payment = session1.find(Payment
                    .class, 1L);

            payment.setAmount(payment.getAmount()+1);
            session1.save(payment);

            session1.getTransaction().commit();

        }
        }
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


