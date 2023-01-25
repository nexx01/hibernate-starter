package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
@Audited
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
//    @NotAudited
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
