package com.travelsuite.controller;

import com.travelsuite.model.Place;
import com.travelsuite.model.PlaceImage;
import com.travelsuite.model.User;
import com.travelsuite.service.PlaceImageService;
import com.travelsuite.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {
    
    @Autowired
    private PlaceImageService imageService;
    
    @Autowired
    private PlaceService placeService;
    
    @GetMapping
    public ResponseEntity<List<PlaceImage>> getAllImages() {
        List<PlaceImage> images = imageService.findAllImages();
        return ResponseEntity.ok(images);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlaceImage> getImageById(@PathVariable Long id) {
        PlaceImage image = imageService.findImageById(id);
        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<PlaceImage>> getImagesByPlace(@PathVariable Long placeId) {
        Place place = placeService.findPlaceById(placeId);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<PlaceImage> images = imageService.findImagesByPlace(place);
        return ResponseEntity.ok(images);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PlaceImage>> searchImages(@RequestParam String keyword) {
        List<PlaceImage> images = imageService.searchImagesByDescription(keyword);
        return ResponseEntity.ok(images);
    }
    
    @PostMapping("/upload/{placeId}")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @PathVariable Long placeId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
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
        
        if (!place.getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only upload images to your own places");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            PlaceImage image = imageService.uploadImage(file, description, place);
            
            response.put("success", true);
            response.put("message", "Image uploaded successfully");
            response.put("image", image);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateImage(
            @PathVariable Long id,
            @RequestBody Map<String, String> imageData,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        PlaceImage image = imageService.findImageById(id);
        if (image == null) {
            response.put("success", false);
            response.put("message", "Image not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        if (!image.getPlace().getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only update your own images");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            image.setDescription(imageData.get("description"));
            imageService.updateImage(image);
            
            response.put("success", true);
            response.put("message", "Image updated successfully");
            response.put("image", image);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        PlaceImage image = imageService.findImageById(id);
        if (image == null) {
            response.put("success", false);
            response.put("message", "Image not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        if (!image.getPlace().getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only delete your own images");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            imageService.deleteImage(image);
            
            response.put("success", true);
            response.put("message", "Image deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

