package com.example.demo.dto;  // Важно: пакет model, а не enums!

import com.example.demo.enums.LocationType;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    
    @Enumerated(EnumType.STRING)
    private LocationType locationType;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_location_id")
    private Location parentLocation;
    
    public Location() {}
    
    public Location(LocationType locationType, String name) {
        this.locationType = locationType;
        this.name = name;
    }
    
    public Location(LocationType locationType, String name, Location parentLocation) {
        this.locationType = locationType;
        this.name = name;
        this.parentLocation = parentLocation;
    }
    
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    
    public LocationType getLocationType() { return locationType; }
    public void setLocationType(LocationType locationType) { this.locationType = locationType; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Location getParentLocation() { return parentLocation; }
    public void setParentLocation(Location parentLocation) { this.parentLocation = parentLocation; }
}