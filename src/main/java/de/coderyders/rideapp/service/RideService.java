package de.coderyders.rideapp.service;

import de.coderyders.rideapp.model.RideReward;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    private static final double CO2_PER_KM = 142.7; // grams of CO2 per km

    public RideReward calculateCO2Savings(RideReward rideReward) {
        double co2Saved = rideReward.getDistance() * CO2_PER_KM * rideReward.getPassengers();
        // Round to 2 decimal places
        rideReward.setCo2Saved(String.format("%.2f", co2Saved));
        return rideReward;
    }
}