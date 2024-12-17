package de.coderyders.rideapp.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String friendId;

    public Friendship(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}