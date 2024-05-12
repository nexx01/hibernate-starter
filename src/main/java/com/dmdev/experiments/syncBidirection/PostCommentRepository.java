package com.dmdev.experiments.syncBidirection;

import com.dmdev.dao.RepositoryBase;
import com.dmdev.entity.User;

import javax.persistence.EntityManager;

public class PostCommentRepository extends RepositoryBase<Long,PostComment> {
    public PostCommentRepository(EntityManager entityManager) {
        super(PostComment.class, entityManager);
    }
}
