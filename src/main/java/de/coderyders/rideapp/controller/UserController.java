package de.coderyders.rideapp.controller;

import de.coderyders.rideapp.model.RideReward;
import de.coderyders.rideapp.model.ShopItem;
import de.coderyders.rideapp.model.User;
import de.coderyders.rideapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        User user = userService.getUser(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable String id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable String userId, @PathVariable String friendId) {
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable String userId, @PathVariable String friendId) {
        userService.deleteFriendship(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/give-points")
    public ResponseEntity<User> givePoints(@PathVariable String id, @RequestParam int points) {
        User updatedUser = userService.givePoints(id, points);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/remove-points")
    public ResponseEntity<User> removePoints(@PathVariable String id, @RequestParam int points) {
        User updatedUser = userService.removePoints(id, points);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rewards")
    public ResponseEntity<Map<String, List<ShopItem>>> getRewards() {
        return ResponseEntity.ok(userService.getRewards());
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<String> redeemReward(@PathVariable String id, @RequestParam String rewardName) {
        String result = userService.redeemReward(id, rewardName);
        if (result.equals("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/{id}/rides/finish")
    public RideReward finishRide(@PathVariable String id, @RequestParam double distance, @RequestParam String[] passengers) {
        return userService.calculateCO2Savings(id, distance, passengers);
    }
}