package de.coderyders.rideapp.controller;

import de.coderyders.rideapp.model.User;
import de.coderyders.rideapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable String userId, @PathVariable String friendId) {
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable String id) {
        return ResponseEntity.ok(userService.getFriends(id));
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
}