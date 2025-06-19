package com.travelsuite.service;

import com.travelsuite.dao.RatingDAO;
import com.travelsuite.model.Rating;
import com.travelsuite.model.Place;
import com.travelsuite.model.PlaceImage;
import com.travelsuite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RatingService {
    
    @Autowired
    private RatingDAO ratingDAO;
    
    public void saveRating(Rating rating) {
        ratingDAO.save(rating);
    }
    
    public void updateRating(Rating rating) {
        ratingDAO.update(rating);
    }
    
    public void deleteRating(Rating rating) {
        ratingDAO.delete(rating);
    }
    
    public Rating findRatingById(Long id) {
        return ratingDAO.findById(id);
    }
    
    public List<Rating> findAllRatings() {
        return ratingDAO.findAll();
    }
    
    public List<Rating> findRatingsByPlace(Place place) {
        return ratingDAO.findByPlace(place);
    }
    
    public List<Rating> findRatingsByImage(PlaceImage image) {
        return ratingDAO.findByImage(image);
    }
    
    public Rating findRatingByUserAndPlace(User user, Place place) {
        return ratingDAO.findByUserAndPlace(user, place);
    }
    
    public Rating findRatingByUserAndImage(User user, PlaceImage image) {
        return ratingDAO.findByUserAndImage(user, image);
    }
    
    public Double getAverageRatingForPlace(Place place) {
        return ratingDAO.getAverageRatingForPlace(place);
    }
    
    public Double getAverageRatingForImage(PlaceImage image) {
        return ratingDAO.getAverageRatingForImage(image);
    }
    
    public Rating ratePlace(User user, Place place, Integer score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5");
        }
        
        Rating existingRating = ratingDAO.findByUserAndPlace(user, place);
        if (existingRating != null) {
            existingRating.setScore(score);
            ratingDAO.update(existingRating);
            return existingRating;
        } else {
            Rating rating = new Rating(score, user, place);
            ratingDAO.save(rating);
            return rating;
        }
    }
    
    public Rating rateImage(User user, PlaceImage image, Integer score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5");
        }
        
        Rating existingRating = ratingDAO.findByUserAndImage(user, image);
        if (existingRating != null) {
            existingRating.setScore(score);
            ratingDAO.update(existingRating);
            return existingRating;
        } else {
            Rating rating = new Rating(score, user, image);
            ratingDAO.save(rating);
            return rating;
        }
    }
}

