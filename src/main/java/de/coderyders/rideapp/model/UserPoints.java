package de.coderyders.rideapp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "userpoints")
public class UserPoints {

    @Id
    private String userId;
    private int points;

}