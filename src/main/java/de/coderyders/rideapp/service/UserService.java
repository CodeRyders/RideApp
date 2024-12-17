package de.coderyders.rideapp.service;

import de.coderyders.rideapp.model.*;
import de.coderyders.rideapp.repository.FriendshipRepository;
import de.coderyders.rideapp.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String API_URL = "https://europe-west3-uryde-dev.cloudfunctions.net/user-getUsers";
    private static final double CO2_PER_KM = 142.7; // grams of CO2 per km

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    // Initial Stuff

    private final List<ShopItem> shopItems = Arrays.asList(
            new ShopItem("Food", "Kaffee", 100),
            new ShopItem("Food", "Schäufele", 350),
            new ShopItem("Food", "Pizza Margarita", 300),
            new ShopItem("Navistimmen", "Santa Claus", 50),
            new ShopItem("Navistimmen", "Kermit", 250),
            new ShopItem("Navistimmen", "Benjamin Blümchen", 250),
            new ShopItem("Skins", "Schlitten", 50),
            new ShopItem("Skins", "Frosch", 100),
            new ShopItem("Skins", "Elefant", 100)
    );


    public List<User> getAllUsers() {
        User[] users = restTemplate.getForObject(API_URL, User[].class);
        List<User> userList = Arrays.asList(users);

        // Update each user with their points
        userList.stream()
                .filter(user -> user != null && user.getId() != null)
                .forEach(user -> {
                    try {
                        UserInfo userInfo = userInfoRepository.findById(user.getId())
                                .orElse(new UserInfo(user.getId()));
                        user.setUserInfo(userInfo);
                    } catch (Exception e) {
                        // Log the error and continue with the next user
                        System.err.println("Error processing user with ID: " + user.getId() + ". Error: " + e.getMessage());
                    }
                });

        return userList;
    }

    public User getUser(String id) {
        if (id == null) {
            return null;
        }

        return getAllUsers().stream()
                .filter(u -> u != null && id.equals(u.getId()))
                .findFirst()
                .orElse(null);
    }


    // Friends Functionality

    public List<User> getFriends(String id) {
        List<String> friendIds = friendshipRepository.findByUserId(id)
                .stream()
                .map(Friendship::getFriendId)
                .toList();

        return getAllUsers().stream()
                .filter(user -> friendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public void addFriend(String userId, String friendId) {
        friendshipRepository.save(new Friendship(userId, friendId));
        friendshipRepository.save(new Friendship(friendId, userId));
    }

    public void deleteFriendship(String userId, String friendId) {
        friendshipRepository.deleteByUserIdAndFriendId(userId, friendId);
        friendshipRepository.deleteByUserIdAndFriendId(friendId, userId);
    }


    // Points Functionality

    public User givePoints(String userId, int points) {
        User user = getUser(userId);
        if (user != null) {
            UserInfo userInfo = user.getUserInfo();
            userInfo.setPoints(userInfo.getPoints() + points);
            userInfoRepository.save(userInfo);
            user.setUserInfo(userInfo);
            return user;
        }
        return null;
    }

    public User removePoints(String userId, int points) {
        User user = getUser(userId);
        if (user != null) {
            UserInfo userInfo = user.getUserInfo();
            int newPoints = Math.max(0, userInfo.getPoints() - points);
            userInfo.setPoints(newPoints);
            userInfoRepository.save(userInfo);
            user.setUserInfo(userInfo);
            return user;
        }
        return null;
    }


    // Rewards Functionality

    public Map<String, List<ShopItem>> getRewards() {
        return shopItems.stream()
                .collect(Collectors.groupingBy(ShopItem::getCategory));
    }

    public String redeemReward(String userId, String rewardName) {
        User user = getUser(userId);
        if (user == null) {
            return "User not found";
        }

        Optional<ShopItem> rewardOpt = shopItems.stream()
                .filter(r -> r.getName().equals(rewardName))
                .findFirst();

        if (rewardOpt.isEmpty()) {
            return "Reward not found";
        }

        ShopItem shopItem = rewardOpt.get();

        if (user.getUserInfo().getPoints() < shopItem.getCost()) {
            return "Not enough points";
        }

        user = removePoints(userId, shopItem.getCost());
        if (user == null) {
            return "Error updating user points";
        }

        return "Success";
    }


    // Co2 Functionality

    public RideReward calculateCO2Savings(String id, double distance, String[] passengers) {
        double co2Saved = distance * CO2_PER_KM * passengers.length;

        RideReward rideReward = new RideReward();
        rideReward.setDriverId(id);
        rideReward.setDistance(distance);
        rideReward.setPassengers(passengers);

        rideReward.setCo2Saved(Math.round(co2Saved * 100.0) / 100.0);

        User user = getUser(id);
        if (user != null) {
            UserInfo userInfo = user.getUserInfo();
            userInfo.setCo2Saved(userInfo.getCo2Saved() + co2Saved);
            userInfoRepository.save(userInfo);
            user.setUserInfo(userInfo);
        }

        return rideReward;
    }
}