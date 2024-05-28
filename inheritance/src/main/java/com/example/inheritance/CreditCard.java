package com.example.inheritance;

import javax.persistence.*;

@Entity
@Table(name = "CREDIT_CARD")
public class CreditCard extends BillingDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "card_number")
    private int cardNumber;

    @Column(name = "exp_month")
    private String expMonth;

    @Column (name = "exp_year")
    private String expYear;

    public CreditCard() {
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardNumber=" + cardNumber +
                ", expMonth='" + expMonth + '\'' +
                ", expYear='" + expYear + '\'' +
                '}';
    }
}