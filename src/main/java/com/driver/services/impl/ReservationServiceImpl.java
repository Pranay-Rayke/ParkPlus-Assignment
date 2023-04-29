package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        try{
            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()){
                throw new Exception("Cannot make reservation");
            }

            //user and parkingLot exists
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();

            boolean checkSpot = false;

            for (Spot spot: spotList){
                if (!spot.getOccupied()){
                    checkSpot = true;
                    break;
                }
            }

            if (!checkSpot){
                throw new Exception("Cannot make reservation");
            }

            SpotType requestedSpotType;
            if (numberOfWheels > 4){
                requestedSpotType = SpotType.OTHERS;
            }
            else if (numberOfWheels > 2) {
                requestedSpotType = SpotType.FOUR_WHEELER;
            }
            else {
                requestedSpotType = SpotType.TWO_WHEELER;
            }

            checkSpot = false;
            int minPrice = Integer.MAX_VALUE;
            Spot minPriceSpot = null;

            for (Spot spot: spotList){
                if (requestedSpotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)){
                    if (spot.getPricePerHour()* timeInHours < minPrice && !spot.getOccupied()){
                        minPrice = spot.getPricePerHour()*timeInHours;
                        checkSpot = true;
                        minPriceSpot = spot;
                    }
                }
                else if (requestedSpotType.equals(SpotType.FOUR_WHEELER) && (spot.getSpotType().equals(SpotType.OTHERS) || spot.getSpotType().equals(SpotType.FOUR_WHEELER))) {
                    if (spot.getPricePerHour()* timeInHours < minPrice && !spot.getOccupied()){
                        minPrice = spot.getPricePerHour()*timeInHours;
                        checkSpot = true;
                        minPriceSpot = spot;
                    }
                }
                else if (requestedSpotType.equals(SpotType.TWO_WHEELER) && (spot.getSpotType().equals(SpotType.OTHERS) || spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.TWO_WHEELER))){
                    if (spot.getPricePerHour()* timeInHours < minPrice && !spot.getOccupied()){
                        minPrice = spot.getPricePerHour()*timeInHours;
                        checkSpot = true;
                        minPriceSpot = spot;
                    }
                }
            }

            if (!checkSpot){
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation = new Reservation();
            minPriceSpot.setOccupied(true);
            reservation.setSpot(minPriceSpot);
            reservation.setNumberOfHours(timeInHours);
            reservation.setUser(user);

            minPriceSpot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(minPriceSpot);

            return reservation;
        }
        catch (Exception e){
            return null;
        }
    }
}