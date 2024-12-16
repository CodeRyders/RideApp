package de.coderyders.rideapp.service;

import de.coderyders.rideapp.model.Location;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LocationService {
    private List<Location> locations = new ArrayList<>();

    @PostConstruct
    public void initializeDefaultLocations() {
        List<Location> defaultLocations = Arrays.asList(
            createLocation("DATEV I", "6-14", "Paumgartnerstraße", "90429", "Nürnberg", 49.45234560916725, 11.048864082875717),
            createLocation("DATEV II", "24", "Fürther Str.", "90429", "Nürnberg", 49.45012544898337, 11.059250105139443),
            createLocation("DATEV ITC", "111", "Fürther Str.", "90429", "Nürnberg", 49.45345377045018, 11.04624465078043),
            createLocation("DATEV III", "172", "Sigmundstraße", "90329", "Nürnberg", 49.44701506121979, 11.008057840704229),
            createLocation("DATEV IIII", "63", "Virnsberger Str.", "90431", "Nürnberg", 49.44747543300277, 11.00621248066461),
            createLocation("DATEV CUBE", "1", "Sophie-Germain-Straße", "90443", "Nürnberg", 49.443861375269144, 11.063429894727383)
        );

        for (Location location : defaultLocations) {
            if (!locationExists(location)) {
                addLocation(location);
            }
        }
    }

    private Location createLocation(String name, String hausnummer, String strasse, String plz, String ort, double breiteKoordinaten, double laengeKoordinaten) {
        Location location = new Location();
        location.setName(name);
        location.setHausnummer(hausnummer);
        location.setStrasse(strasse);
        location.setPlz(plz);
        location.setOrt(ort);
        location.setBreiteKoordinaten(breiteKoordinaten);
        location.setLaengeKoordinaten(laengeKoordinaten);
        return location;
    }

    private boolean locationExists(Location location) {
        return locations.stream()
                .anyMatch(l -> l.getName().equals(location.getName()) &&
                        l.getStrasse().equals(location.getStrasse()) &&
                        l.getHausnummer().equals(location.getHausnummer()));
    }

    public Location addLocation(Location location) {
        location.setId(UUID.randomUUID().toString());
        locations.add(location);
        return location;
    }

    public void deleteLocation(String locationId) {
        locations.removeIf(location -> location.getId().equals(locationId));
    }

    public List<Location> getAllLocations() {
        return new ArrayList<>(locations);
    }

    public Optional<Location> getLocation(String locationId) {
        return locations.stream()
                .filter(location -> location.getId().equals(locationId))
                .findFirst();
    }
}