package de.coderyders.rideapp.repository;

import de.coderyders.rideapp.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUserId(String userId);
    void deleteByUserIdAndFriendId(String userId, String friendId);
}