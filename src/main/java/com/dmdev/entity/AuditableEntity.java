package com.dmdev.entity;

import com.dmdev.listeners.AuditListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {

    private Instant createdAt;

    private String createdBy;


    private Instant updatedAt;

    private String updatedBy;
//
//
//    @PrePersist
//    public void prePersist() {
//        setCreatedAt(Instant.now());
////        setCreatedBy(SecurityContext.getUser);
//
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        setUpdatedAt(Instant.now());
////        setUpdatedAt(SecurityContext.getUser);
//    }
}
