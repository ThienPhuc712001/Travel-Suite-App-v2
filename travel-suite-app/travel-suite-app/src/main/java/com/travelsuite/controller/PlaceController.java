package com.travelsuite.controller;

import com.travelsuite.model.Place;
import com.travelsuite.model.User;
import com.travelsuite.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/places")
@CrossOrigin(origins = "*")
public class PlaceController {
    
    @Autowired
    private PlaceService placeService;
    
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeService.findAllPlaces();
        return ResponseEntity.ok(places);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Place place = placeService.findPlaceById(id);
        if (place != null) {
            return ResponseEntity.ok(place);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Place>> searchPlaces(@RequestParam String keyword) {
        List<Place> places = placeService.searchPlacesByKeyword(keyword);
        return ResponseEntity.ok(places);
    }
    
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyPlaces(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        List<Place> places = placeService.findPlacesByUser(user);
        response.put("success", true);
        response.put("places", places);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlace(@RequestBody Map<String, String> placeData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        if (user.getRole() != User.UserRole.GUIDE) {
            response.put("success", false);
            response.put("message", "Only guides can create places");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            String name = placeData.get("name");
            String description = placeData.get("description");
            String location = placeData.get("location");
            
            Place place = placeService.createPlace(name, description, location, user);
            
            response.put("success", true);
            response.put("message", "Place created successfully");
            response.put("place", place);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create place: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePlace(@PathVariable Long id, @RequestBody Map<String, String> placeData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        Place place = placeService.findPlaceById(id);
        if (place == null) {
            response.put("success", false);
            response.put("message", "Place not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        if (!place.getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only update your own places");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            place.setName(placeData.get("name"));
            place.setDescription(placeData.get("description"));
            place.setLocation(placeData.get("location"));
            
            placeService.updatePlace(place);
            
            response.put("success", true);
            response.put("message", "Place updated successfully");
            response.put("place", place);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update place: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePlace(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        Place place = placeService.findPlaceById(id);
        if (place == null) {
            response.put("success", false);
            response.put("message", "Place not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        if (!place.getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only delete your own places");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            placeService.deletePlace(place);
            
            response.put("success", true);
            response.put("message", "Place deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete place: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

