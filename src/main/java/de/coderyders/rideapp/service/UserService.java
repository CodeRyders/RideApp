package de.coderyders.rideapp.service;

import de.coderyders.rideapp.model.User;
import de.coderyders.rideapp.model.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.addFriend(friend);
        userRepository.save(user);
    }

    public List<User> getFriends(Long id) {
        User user = getUser(id);
        return user.getFriends().stream().collect(Collectors.toList());
    }
}