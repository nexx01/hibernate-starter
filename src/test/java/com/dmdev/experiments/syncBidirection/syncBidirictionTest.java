package com.dmdev.experiments.syncBidirection;

import com.dmdev.util.HibernateTestUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class syncBidirictionTest {

//    private E

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

//    private final PostRepository postRepository =new PostRepository(sessionFactory);


    @Test
    void name() {
        var post = new Post();
        post.setTitle("High-Perfomance Java Persistence");
        post.setId(1L);

        var postComment = new PostComment();
        postComment.setReview("JPA and Hibernate");
        post.asddComment(postComment);
        var session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(post);
        session.getTransaction().commit();
        session.close();

        var session1 = sessionFactory.openSession();
        var actual = session1.get(Post.class, 1L);
        session1.close();

        assertThat(actual.getTitle()).isEqualTo(post.getTitle());

    }

    @Test
    void should_manyToOne_ad_kid_entity_without_add_to_parent() {
        var post = new Post();
        post.setTitle("High-Perfomance Java Persistence");
        post.setId(1L);

        var postComment = new PostComment();
        postComment.setReview("JPA and Hibernate");
//        postComment.setPost(post);
        post.asddComment(postComment);

        var postComment1 = new PostComment();
        postComment1.setReview("this is reqiew additional postcomment");
        postComment1.setPost(post);

        var session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(post);
        session.save(postComment);
        session.save(postComment1);
        session.getTransaction().commit();
        session.close();

        var session1 = sessionFactory.openSession();
        session1.beginTransaction();
        var actual = session1.get(Post.class, 1L);

        assertThat(actual.getTitle()).isEqualTo(post.getTitle());
        assertThat(actual.getComments()).isNotEmpty().singleElement()
                .satisfies(p -> p.getReview().equals(postComment.getReview()));
        session1.close();

    }

    @Test
    void should_manyToOne_save_kid_entity_without_parent() {

        var postComment = new PostComment();
        postComment.setId(1L);
        postComment.setReview("JPA and Hibernate");
        var session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(postComment);
        session.getTransaction().commit();
        session.close();

        var session1 = sessionFactory.openSession();
        var actual = session1.get(PostComment.class, 1L);
        session1.close();

        assertThat(actual.getReview()).isEqualTo(postComment.getReview());
    }


    @Test
    void syncOneToOne() {
        var post2 = new Post2();
        post2.setId(1L);
        post2.setTitle("High-Perfomance Java Persintence");

        var postDetails = new PostDetails();
        postDetails.setCreatedBy("vlad Mihalcea");

        post2.setDetails(postDetails);
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.save(post2);
        transaction.commit();
        session.close();

        var session1 = sessionFactory.openSession();
        var actual = session1.get(Post2.class, 1L);

        assertThat(actual.getTitle()).isEqualTo(post2.getTitle());
    }
}
