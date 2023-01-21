package com.dmdev;

import com.dmdev.entity.Company;
import com.dmdev.entity.User;
import com.dmdev.entity.UserChat;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.jpa.QueryHints;

import javax.persistence.QueryHint;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

                System.out.println("----------------Without Annotation EntityGrath----------------");

                RootGraph<User> entityGraph = session1.createEntityGraph(
                        User.class
                );
                entityGraph.addAttributeNodes("company", "userChats");
                SubGraph<UserChat> userChats = entityGraph.addSubGraph("userChats", UserChat.class);
                userChats.addAttributeNodes("chat");

//                session1.enableFetchProfile("withCompanyAndPayments");
////                session1.save(user);

                Map<String, Object> properties = Map.of(
                        GraphSemantic.LOAD.getJpaHintName(), entityGraph);

                User user1 = session1.find(User.class, 1L,properties);

                System.out.println(user1.getCompany().getName());
                System.out.println(user1.getUserChats().size());
                System.out.println(user1.getPayments());

                List<User> users = session1.createQuery("select u from User u " +
                                "where 1=1", User.class)
                        .setHint(GraphSemantic.LOAD.getJpaHintName(),entityGraph)
                        .list();
                users.forEach(user -> System.out.println(user.getUserChats().size()));
                users.forEach(user -> System.out.println(user.getCompany().getName()));


                System.out.println("----------------------------------------------");
                System.out.println("----------------------------------------------");
                System.out.println("----------------------------------------------");
                System.out.println("----------------With Annotation EntityGrath----------------");
//                ---------------------------------------------------/
                //with annotation
//
//                Map<String, Object> properties3 = Map.of(
//                        GraphSemantic.LOAD.getJpaHintName(), session1.getEntityGraph("withCompanyAndChat"));
//
//                User user3 = session1.find(User.class, 1L,properties3);
//
//                System.out.println(user3.getCompany().getName());
//                System.out.println(user3.getUserChats().size());
//                System.out.println(user3.getPayments());
//
//                List<User> users3 = session1.createQuery("select u from User u " +
//                                "where 1=1", User.class)
//                        .setHint(GraphSemantic.LOAD.getJpaHintName(),session1.getEntityGraph("withCompanyAndChat"))
//                        .list();
//                users.forEach(user -> System.out.println(user.getUserChats().size()));
//                users.forEach(user -> System.out.println(user.getCompany().getName()));

                session1.getTransaction().commit();
            }
        }
    }
}



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
 */


