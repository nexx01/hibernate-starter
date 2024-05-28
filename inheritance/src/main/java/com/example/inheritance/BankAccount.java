package com.example.inheritance;

import javax.persistence.*;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount extends BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private int account;

    @Column(name = "bank_name")
    private String bankName;

    private String swift;

    public BankAccount() {
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "account=" + account +
                ", bankName='" + bankName + '\'' +
                ", swift='" + swift + '\'' +
                '}';
    }
}