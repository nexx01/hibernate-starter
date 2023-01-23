package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@OptimisticLocking(type = OptimisticLockType.ALL)
//@OptimisticLocking(type = OptimisticLockType.DIRTY)
//@DynamicUpdate
public class Payment extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;
    @Version
    private Long version;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;
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
