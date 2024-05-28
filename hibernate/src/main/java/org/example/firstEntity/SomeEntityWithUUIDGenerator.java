package org.example.firstEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity

public class SomeEntityWithUUIDGenerator {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String foo;

    private String bar;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFoo() {
        return foo;
    }

    @Override
    public String toString() {
        return "SomeEntityWithUUIDGenerator{" +
                "id=" + id +
                ", foo='" + foo + '\'' +
                ", bar='" + bar + '\'' +
                '}';
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
