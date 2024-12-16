package de.coderyders.rideapp.model;

public class User {
    private String id;
    private String customerId;
    private int maxDetourInPercent;

    // Constructors, getters, and setters
    public User() {}

    public User(String id, String customerId, int maxDetourInPercent) {
        this.id = id;
        this.customerId = customerId;
        this.maxDetourInPercent = maxDetourInPercent;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getMaxDetourInPercent() {
        return maxDetourInPercent;
    }

    public void setMaxDetourInPercent(int maxDetourInPercent) {
        this.maxDetourInPercent = maxDetourInPercent;
    }
}