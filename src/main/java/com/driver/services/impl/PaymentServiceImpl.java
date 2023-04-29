package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Payment payment = new Payment();
        payment.setReservation(reservationRepository2.findById(reservationId).get());
        if(amountSent<reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour()){
            throw new Exception("Insufficient Amount");
        }
        if(mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi") ){
            if(mode.equalsIgnoreCase("cash")){
                payment.setPaymentMode(PaymentMode.CASH);
            }
            else if(mode.equalsIgnoreCase("card")){
                payment.setPaymentMode(PaymentMode.CARD);
            }
            else payment.setPaymentMode(PaymentMode.UPI);
            payment.setPaymentCompleted(true);
            payment.setReservation(reservation);
            reservation.setPayment(payment);
            reservationRepository2.save(reservation);
            return payment;
        }
        else throw new Exception("Payment mode not detected");
    }
}