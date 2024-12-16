package de.coderyders.rideapp.repository;

import de.coderyders.rideapp.model.UserPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointsRepository extends JpaRepository<UserPoints, String> {
}