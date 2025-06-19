package com.travelsuite.controller;

import com.travelsuite.model.*;
import com.travelsuite.service.CommentService;
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
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PlaceService placeService;
    
    @Autowired
    private PlaceImageService imageService;
    
    @PostMapping("/place/{placeId}")
    public ResponseEntity<Map<String, Object>> commentOnPlace(
            @PathVariable Long placeId,
            @RequestBody Map<String, String> commentData,
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
            String content = commentData.get("content");
            Comment comment = commentService.addCommentToPlace(user, place, content);
            
            response.put("success", true);
            response.put("message", "Comment added successfully");
            response.put("comment", comment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to add comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/image/{imageId}")
    public ResponseEntity<Map<String, Object>> commentOnImage(
            @PathVariable Long imageId,
            @RequestBody Map<String, String> commentData,
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
            String content = commentData.get("content");
            Comment comment = commentService.addCommentToImage(user, image, content);
            
            response.put("success", true);
            response.put("message", "Comment added successfully");
            response.put("comment", comment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to add comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<Comment>> getPlaceComments(@PathVariable Long placeId) {
        Place place = placeService.findPlaceById(placeId);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Comment> comments = commentService.findCommentsByPlace(place);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/image/{imageId}")
    public ResponseEntity<List<Comment>> getImageComments(@PathVariable Long imageId) {
        PlaceImage image = imageService.findImageById(imageId);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Comment> comments = commentService.findCommentsByImage(image);
        return ResponseEntity.ok(comments);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long id,
            @RequestBody Map<String, String> commentData,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        Comment comment = commentService.findCommentById(id);
        if (comment == null) {
            response.put("success", false);
            response.put("message", "Comment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        if (!comment.getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only update your own comments");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            comment.setContent(commentData.get("content"));
            commentService.updateComment(comment);
            
            response.put("success", true);
            response.put("message", "Comment updated successfully");
            response.put("comment", comment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please login first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        Comment comment = commentService.findCommentById(id);
        if (comment == null) {
            response.put("success", false);
            response.put("message", "Comment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        if (!comment.getUser().getId().equals(user.getId())) {
            response.put("success", false);
            response.put("message", "You can only delete your own comments");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        try {
            commentService.deleteComment(comment);
            
            response.put("success", true);
            response.put("message", "Comment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

