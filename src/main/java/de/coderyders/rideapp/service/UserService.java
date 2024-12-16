package de.coderyders.rideapp.service;

import de.coderyders.rideapp.model.Friendship;
import de.coderyders.rideapp.model.User;
import de.coderyders.rideapp.repository.FriendshipRepository;
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

    public List<User> getAllUsers() {
        User[] users = restTemplate.getForObject(API_URL, User[].class);
        return Arrays.asList(users);
    }

    public User getUser(String id) {
        return getAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
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
}