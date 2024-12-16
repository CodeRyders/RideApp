package de.coderyders.rideapp.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    private String id;
    private String name;
    private String hausnummer;
    private String strasse;
    private String plz;
    private String ort;
    private double laengeKoordinaten;
    private double breiteKoordinaten;
}