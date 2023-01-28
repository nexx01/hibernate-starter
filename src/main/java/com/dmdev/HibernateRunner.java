package com.dmdev;

import com.dmdev.dao.CompanyRepository;
import com.dmdev.dao.PaymentRepository;
import com.dmdev.dao.UserRepository;
import com.dmdev.dto.UserCreateDTO;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.interceptor.TransactionInterceptor;
import com.dmdev.mapper.CompanyReadMapper;
import com.dmdev.mapper.UserCreateDtoMapper;
import com.dmdev.mapper.UserReadDtoMapper;
import com.dmdev.service.UserService;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {


    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            TestDataImporter.importData(sessionFactory);
            User user = null;
//            try (var session = sessionFactory.openSession()) {
//            var session = sessionFactory.getCurrentSession(); //При использовании ThreadLocalCurrentContext
            //не обязательно закрывать сессию она сама закроется при коммит или ролбек

            //Threadlocal не работает при неблокирующих стратегиях
            //example with springReactive

            var session = (Session)Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(sessionFactory.getCurrentSession(), args);
                }
            });
//                session.beginTransaction();

            var companyRepository = new CompanyRepository(session);

            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadDtoMapper userReadDtoMapper = new UserReadDtoMapper(companyReadMapper);
            UserCreateDtoMapper userCreateDtoMapper = new UserCreateDtoMapper(companyRepository);


            UserRepository userRepository = new UserRepository(session);
//            UserService userService =
//                    new UserService(userRepository, userReadDtoMapper, userCreateDtoMapper);

            var transactionalInterceptor = new TransactionInterceptor(sessionFactory);
            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionalInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadDtoMapper.class, UserCreateDtoMapper.class)
                    .newInstance(userRepository, userReadDtoMapper, userCreateDtoMapper);


            var userCreateDTO = new UserCreateDTO(
                    PersonalInfo.builder()
                            .firstname("Liza")
                            .lastname("Stepanova")
                            .birthDate(LocalDate.now())
                            .build(),
                    "lisa@gmail.com"
                            ,null,
//                    Role.USER,
                    null,
                    1
            );


//            var userCreateDTO2 = new UserCreateDTO(
//                    PersonalInfo.builder()
//                            .firstname("Liza")
//                            .lastname("Stepanova")
//                            .birthDate(LocalDate.now())
//                            .build(),
//                    "lisa2@gmail.com"
//                    ,null,
//                    Role.USER,
//                    1
//            );
            userService.create(userCreateDTO);
//            userService.create(userCreateDTO2);
//            userService.findById(1L).ifPresent(System.out::println);




            var paymentRepository = new PaymentRepository(session);
//                paymentRepository.findById(1L).ifPresent(System.out::println);
//
//                session.getTransaction().commit();
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


