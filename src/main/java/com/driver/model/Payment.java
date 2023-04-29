package com.driver.model;

import javax.persistence.*;

@Entity
@Table
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    public boolean isPaymentCompleted;
    public void setPaymentCompleted(boolean paymentCompleted){
        this.isPaymentCompleted=paymentCompleted;
    }
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    @OneToOne
    @JoinColumn
    private Reservation reservation;

    public Payment(){}

    public Payment(boolean paymentCompleted, PaymentMode paymentMode){
        setPaymentMode(paymentMode);
        this.isPaymentCompleted = paymentCompleted;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setPaymentMode(PaymentMode paymentMode) {

        this.paymentMode = paymentMode;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public int getId() {
        return id;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

}