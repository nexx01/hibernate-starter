package com.dmdev.cache;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY,region = "department")
public class DepartmentEntity {
    @Id
    private Long id;

    private String name;
}
