package com.travelsuite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "place_images")
public class PlaceImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String filename;
    
    @Column(nullable = false)
    private String originalName;
    
    private String description;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "place_id")
    private Place place;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;
    
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings;
    
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
    
    // Constructors
    public PlaceImage() {
        this.uploadedAt = new Date();
    }
    
    public PlaceImage(String filename, String originalName, String description, Place place) {
        this();
        this.filename = filename;
        this.originalName = originalName;
        this.description = description;
        this.place = place;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Place getPlace() { return place; }
    public void setPlace(Place place) { this.place = place; }
    
    public Date getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Date uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public List<Rating> getRatings() { return ratings; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }
    
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}

