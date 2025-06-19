package com.travelsuite.controller;

import com.travelsuite.model.*;
import com.travelsuite.service.RatingService;
import com.travelsuite.service.PlaceService;
import com.travelsuite.service.PlaceImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @Autowired
    private PlaceService placeService;
    
    @Autowired
    private PlaceImageService imageService;
    
    @PostMapping("/place/{placeId}")
    public ResponseEntity<Map<String, Object>> ratePlace(
            @PathVariable Long placeId,
            @RequestBody Map<String, Integer> ratingData,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        Place place = placeService.findPlaceById(placeId);
        if (place == null) {
            response.put("success", false);
            response.put("message", "Place not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        try {
            Integer score = ratingData.get("score");
            Rating rating = ratingService.ratePlace(user, place, score);
            
            response.put("success", true);
            response.put("message", "Place rated successfully");
            response.put("rating", rating);
            response.put("averageRating", ratingService.getAverageRatingForPlace(place));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to rate place: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/image/{imageId}")
    public ResponseEntity<Map<String, Object>> rateImage(
            @PathVariable Long imageId,
            @RequestBody Map<String, Integer> ratingData,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        PlaceImage image = imageService.findImageById(imageId);
        if (image == null) {
            response.put("success", false);
            response.put("message", "Image not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        try {
            Integer score = ratingData.get("score");
            Rating rating = ratingService.rateImage(user, image, score);
            
            response.put("success", true);
            response.put("message", "Image rated successfully");
            response.put("rating", rating);
            response.put("averageRating", ratingService.getAverageRatingForImage(image));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to rate image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/place/{placeId}")
    public ResponseEntity<Map<String, Object>> getPlaceRatings(@PathVariable Long placeId) {
        Place place = placeService.findPlaceById(placeId);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Rating> ratings = ratingService.findRatingsByPlace(place);
        Double averageRating = ratingService.getAverageRatingForPlace(place);
        
        Map<String, Object> response = new HashMap<>();
        response.put("ratings", ratings);
        response.put("averageRating", averageRating);
        response.put("totalRatings", ratings.size());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/image/{imageId}")
    public ResponseEntity<Map<String, Object>> getImageRatings(@PathVariable Long imageId) {
        PlaceImage image = imageService.findImageById(imageId);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Rating> ratings = ratingService.findRatingsByImage(image);
        Double averageRating = ratingService.getAverageRatingForImage(image);
        
        Map<String, Object> response = new HashMap<>();
        response.put("ratings", ratings);
        response.put("averageRating", averageRating);
        response.put("totalRatings", ratings.size());
        
        return ResponseEntity.ok(response);
    }
}

