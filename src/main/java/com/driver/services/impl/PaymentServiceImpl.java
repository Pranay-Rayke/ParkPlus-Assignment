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

        //reservationId is valid always
        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int bill = reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour();
        if (amountSent< bill){
            throw new Exception("Insufficient Amount");
        }
        else {
            if (mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi")){

                //main logic starts here
                Payment payment = new Payment();

                //setting payment mode
                if (mode.equalsIgnoreCase("cash")){
                    payment.setPaymentMode(PaymentMode.CASH);
                }
                else if (mode.equalsIgnoreCase("card")){
                    payment.setPaymentMode(PaymentMode.CARD);
                }
                else {
                    payment.setPaymentMode(PaymentMode.UPI);
                }

                payment.setPaymentCompleted(true);
                payment.setReservation(reservation);

                //bidirectional
                reservation.setPayment(payment);

                //saving the parent entity
                reservationRepository2.save(reservation);
                return payment;
            }
            else {
                throw new Exception("Payment mode not detected");
            }
        }
    }
}