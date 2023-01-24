package com.dmdev.listeners;

import com.dmdev.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class SomeRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
//        SecurityContext.getUser().getId();
        ((Revision)revisionEntity).setUsername("someUser");
    }
}
