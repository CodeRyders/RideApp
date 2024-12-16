package de.coderyders.rideapp.service;

import de.coderyders.rideapp.model.Friendship;
import de.coderyders.rideapp.model.User;
import de.coderyders.rideapp.model.UserPoints;
import de.coderyders.rideapp.repository.FriendshipRepository;
import de.coderyders.rideapp.repository.UserPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String API_URL = "https://europe-west3-uryde-dev.cloudfunctions.net/user-getUsers";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserPointsRepository userPointsRepository;

    public List<User> getAllUsers() {
        User[] users = restTemplate.getForObject(API_URL, User[].class);
        List<User> userList = Arrays.asList(users);

        // Update each user with their points
        userList.stream()
                .filter(user -> user != null && user.getId() != null)
                .forEach(user -> {
                    try {
                        UserPoints userPoints = userPointsRepository.findById(user.getId())
                                .orElse(new UserPoints(user.getId(), 0));
                        user.setPoints(userPoints.getPoints());
                    } catch (Exception e) {
                        // Log the error and continue with the next user
                        System.err.println("Error processing user with ID: " + user.getId() + ". Error: " + e.getMessage());
                    }
                });

        return userList;
    }

    public void addFriend(String userId, String friendId) {
        friendshipRepository.save(new Friendship(userId, friendId));
        friendshipRepository.save(new Friendship(friendId, userId));
    }

    public List<User> getFriends(String id) {
        List<String> friendIds = friendshipRepository.findByUserId(id)
                .stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());

        return getAllUsers().stream()
                .filter(user -> friendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public void deleteFriendship(String userId, String friendId) {
        friendshipRepository.deleteByUserIdAndFriendId(userId, friendId);
        friendshipRepository.deleteByUserIdAndFriendId(friendId, userId);
    }

    public User givePoints(String userId, int points) {
        User user = getUser(userId);
        if (user != null) {
            UserPoints userPoints = userPointsRepository.findById(userId)
                    .orElse(new UserPoints(userId, 0));
            userPoints.setPoints(userPoints.getPoints() + points);
            userPointsRepository.save(userPoints);
            user.setPoints(userPoints.getPoints());
            return user;
        }
        return null;
    }

    public User removePoints(String userId, int points) {
        User user = getUser(userId);
        if (user != null) {
            UserPoints userPoints = userPointsRepository.findById(userId)
                    .orElse(new UserPoints(userId, 0));
            int newPoints = Math.max(0, userPoints.getPoints() - points);
            userPoints.setPoints(newPoints);
            userPointsRepository.save(userPoints);
            user.setPoints(newPoints);
            return user;
        }
        return null;
    }

    public User getUser(String id) {
        User user = getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (user != null) {
            UserPoints userPoints = userPointsRepository.findById(id)
                    .orElse(new UserPoints(id, 0));
            user.setPoints(userPoints.getPoints());
        }
        return user;
    }
}