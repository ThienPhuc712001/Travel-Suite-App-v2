package com.travelsuite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer score; // 1-5 stars
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "place_id")
    private Place place;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "image_id")
    private PlaceImage image;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    // Constructors
    public Rating() {
        this.createdAt = new Date();
    }
    
    public Rating(Integer score, User user, Place place) {
        this();
        this.score = score;
        this.user = user;
        this.place = place;
    }
    
    public Rating(Integer score, User user, PlaceImage image) {
        this();
        this.score = score;
        this.user = user;
        this.image = image;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Place getPlace() { return place; }
    public void setPlace(Place place) { this.place = place; }
    
    public PlaceImage getImage() { return image; }
    public void setImage(PlaceImage image) { this.image = image; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

