package com.travelsuite.dao;

import com.travelsuite.model.Place;
import com.travelsuite.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public void save(Place place) {
        Session session = sessionFactory.getCurrentSession();
        session.save(place);
    }
    
    public void update(Place place) {
        Session session = sessionFactory.getCurrentSession();
        session.update(place);
    }
    
    public void delete(Place place) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(place);
    }
    
    public Place findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Place.class, id);
    }
    
    public List<Place> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Place> query = session.createQuery("FROM Place ORDER BY createdAt DESC", Place.class);
        return query.list();
    }
    
    public List<Place> findByUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query<Place> query = session.createQuery("FROM Place WHERE user = :user ORDER BY createdAt DESC", Place.class);
        query.setParameter("user", user);
        return query.list();
    }
    
    public List<Place> searchByName(String keyword) {
        Session session = sessionFactory.getCurrentSession();
        Query<Place> query = session.createQuery("FROM Place WHERE name LIKE :keyword ORDER BY createdAt DESC", Place.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.list();
    }
    
    public List<Place> searchByLocation(String location) {
        Session session = sessionFactory.getCurrentSession();
        Query<Place> query = session.createQuery("FROM Place WHERE location LIKE :location ORDER BY createdAt DESC", Place.class);
        query.setParameter("location", "%" + location + "%");
        return query.list();
    }
    
    public List<Place> searchByKeyword(String keyword) {
        Session session = sessionFactory.getCurrentSession();
        Query<Place> query = session.createQuery(
            "FROM Place WHERE name LIKE :keyword OR description LIKE :keyword OR location LIKE :keyword ORDER BY createdAt DESC", 
            Place.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.list();
    }
}

