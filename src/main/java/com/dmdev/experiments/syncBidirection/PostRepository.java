package com.dmdev.experiments.syncBidirection;

import com.dmdev.dao.RepositoryBase;
import com.dmdev.entity.User;

import javax.persistence.EntityManager;

public class PostRepository  extends RepositoryBase<Long,Post> {


    public PostRepository(EntityManager entityManager) {
        super(Post.class, entityManager);
    }
}
