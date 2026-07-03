package com.primezat.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "travel_package")
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;
    private String name;
    private String destination;
    private String country;
    private String duration;
    private Double price;
    private Integer discount;

    @Column(columnDefinition = "TEXT")
    private String imagesJson;

    @Column(columnDefinition = "TEXT")
    private String videosJson;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String inclusions;

    @Column(columnDefinition = "TEXT")
    private String exclusions;

    @Column(columnDefinition = "TEXT")
    private String itineraryJson;

    private String pickup;
    private String dropPoint;
    private String meals;
    private String hotel;
    private Boolean flightIncluded;
    private Boolean guideIncluded;
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public TravelPackage() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDiscount() { return discount; }
    public void setDiscount(Integer discount) { this.discount = discount; }

    public String getImagesJson() { return imagesJson; }
    public void setImagesJson(String imagesJson) { this.imagesJson = imagesJson; }

    public String getVideosJson() { return videosJson; }
    public void setVideosJson(String videosJson) { this.videosJson = videosJson; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInclusions() { return inclusions; }
    public void setInclusions(String inclusions) { this.inclusions = inclusions; }

    public String getExclusions() { return exclusions; }
    public void setExclusions(String exclusions) { this.exclusions = exclusions; }

    public String getItineraryJson() { return itineraryJson; }
    public void setItineraryJson(String itineraryJson) { this.itineraryJson = itineraryJson; }

    public String getPickup() { return pickup; }
    public void setPickup(String pickup) { this.pickup = pickup; }

    public String getDropPoint() { return dropPoint; }
    public void setDropPoint(String dropPoint) { this.dropPoint = dropPoint; }

    public String getMeals() { return meals; }
    public void setMeals(String meals) { this.meals = meals; }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public Boolean getFlightIncluded() { return flightIncluded; }
    public void setFlightIncluded(Boolean flightIncluded) { this.flightIncluded = flightIncluded; }

    public Boolean getGuideIncluded() { return guideIncluded; }
    public void setGuideIncluded(Boolean guideIncluded) { this.guideIncluded = guideIncluded; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
