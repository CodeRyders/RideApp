package de.coderyders.rideapp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "userinfo")
public class UserInfo {

    @Id
    private String userId;
    private int points;
    private double co2Saved;

    public UserInfo(String userId) {
        this.userId = userId;
    }
}