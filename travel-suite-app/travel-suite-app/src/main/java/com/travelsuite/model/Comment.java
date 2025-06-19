package com.travelsuite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
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
    public Comment() {
        this.createdAt = new Date();
    }
    
    public Comment(String content, User user, Place place) {
        this();
        this.content = content;
        this.user = user;
        this.place = place;
    }
    
    public Comment(String content, User user, PlaceImage image) {
        this();
        this.content = content;
        this.user = user;
        this.image = image;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Place getPlace() { return place; }
    public void setPlace(Place place) { this.place = place; }
    
    public PlaceImage getImage() { return image; }
    public void setImage(PlaceImage image) { this.image = image; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

