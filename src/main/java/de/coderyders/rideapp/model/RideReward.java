package de.coderyders.rideapp.model;

import lombok.Data;

@Data
public class RideReward {
    private Long id;
    private double distance;
    private int passengers;
    private String co2Saved;
}