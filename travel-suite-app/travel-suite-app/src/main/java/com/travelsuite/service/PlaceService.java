package com.travelsuite.service;

import com.travelsuite.dao.PlaceDAO;
import com.travelsuite.model.Place;
import com.travelsuite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlaceService {
    
    @Autowired
    private PlaceDAO placeDAO;
    
    public void savePlace(Place place) {
        placeDAO.save(place);
    }
    
    public void updatePlace(Place place) {
        placeDAO.update(place);
    }
    
    public void deletePlace(Place place) {
        placeDAO.delete(place);
    }
    
    public Place findPlaceById(Long id) {
        return placeDAO.findById(id);
    }
    
    public List<Place> findAllPlaces() {
        return placeDAO.findAll();
    }
    
    public List<Place> findPlacesByUser(User user) {
        return placeDAO.findByUser(user);
    }
    
    public List<Place> searchPlacesByName(String keyword) {
        return placeDAO.searchByName(keyword);
    }
    
    public List<Place> searchPlacesByLocation(String location) {
        return placeDAO.searchByLocation(location);
    }
    
    public List<Place> searchPlacesByKeyword(String keyword) {
        return placeDAO.searchByKeyword(keyword);
    }
    
    public Place createPlace(String name, String description, String location, User user) {
        Place place = new Place(name, description, location, user);
        placeDAO.save(place);
        return place;
    }
}

