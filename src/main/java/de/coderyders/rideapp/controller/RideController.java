package de.coderyders.rideapp.controller;

import de.coderyders.rideapp.model.RideReward;
import de.coderyders.rideapp.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/finish")
    public RideReward finishRide(@RequestBody RideReward rideReward) {
        return rideService.calculateCO2Savings(rideReward);
    }
}