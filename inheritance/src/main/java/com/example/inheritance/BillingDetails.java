package com.example.inheritance;

import javax.persistence.*;

@MappedSuperclass
public abstract class BillingDetails {

    private String owner;

    public BillingDetails() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "BillingDetails{" +
                "owner='" + owner + '\'' +
                '}';
    }
}