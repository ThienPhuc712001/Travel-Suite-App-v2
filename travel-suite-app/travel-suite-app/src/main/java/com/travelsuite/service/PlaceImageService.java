package com.travelsuite.service;

import com.travelsuite.dao.PlaceImageDAO;
import com.travelsuite.model.Place;
import com.travelsuite.model.PlaceImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PlaceImageService {
    
    @Autowired
    private PlaceImageDAO placeImageDAO;
    
    private static final String UPLOAD_DIR = "uploads/";
    
    public void saveImage(PlaceImage image) {
        placeImageDAO.save(image);
    }
    
    public void updateImage(PlaceImage image) {
        placeImageDAO.update(image);
    }
    
    public void deleteImage(PlaceImage image) {
        // Delete file from filesystem
        File file = new File(UPLOAD_DIR + image.getFilename());
        if (file.exists()) {
            file.delete();
        }
        placeImageDAO.delete(image);
    }
    
    public PlaceImage findImageById(Long id) {
        return placeImageDAO.findById(id);
    }
    
    public List<PlaceImage> findAllImages() {
        return placeImageDAO.findAll();
    }
    
    public List<PlaceImage> findImagesByPlace(Place place) {
        return placeImageDAO.findByPlace(place);
    }
    
    public List<PlaceImage> searchImagesByDescription(String keyword) {
        return placeImageDAO.searchByDescription(keyword);
    }
    
    public PlaceImage uploadImage(MultipartFile file, String description, Place place) throws IOException {
        // Create upload directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        // Save file to filesystem
        File destFile = new File(UPLOAD_DIR + filename);
        file.transferTo(destFile);
        
        // Save image info to database
        PlaceImage image = new PlaceImage(filename, originalFilename, description, place);
        placeImageDAO.save(image);
        
        return image;
    }
}

