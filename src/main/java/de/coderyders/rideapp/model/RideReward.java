package de.coderyders.rideapp.model;

import lombok.Data;

@Data
public class RideReward {
    private String driverId;
    private String[] passengers;
    private double distance;
    private double co2Saved;
}