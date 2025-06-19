package com.travelsuite.dao;

import com.travelsuite.model.Rating;
import com.travelsuite.model.Place;
import com.travelsuite.model.PlaceImage;
import com.travelsuite.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RatingDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public void save(Rating rating) {
        Session session = sessionFactory.getCurrentSession();
        session.save(rating);
    }
    
    public void update(Rating rating) {
        Session session = sessionFactory.getCurrentSession();
        session.update(rating);
    }
    
    public void delete(Rating rating) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(rating);
    }
    
    public Rating findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Rating.class, id);
    }
    
    public List<Rating> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("FROM Rating ORDER BY createdAt DESC", Rating.class);
        return query.list();
    }
    
    public List<Rating> findByPlace(Place place) {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("FROM Rating WHERE place = :place ORDER BY createdAt DESC", Rating.class);
        query.setParameter("place", place);
        return query.list();
    }
    
    public List<Rating> findByImage(PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("FROM Rating WHERE image = :image ORDER BY createdAt DESC", Rating.class);
        query.setParameter("image", image);
        return query.list();
    }
    
    public Rating findByUserAndPlace(User user, Place place) {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("FROM Rating WHERE user = :user AND place = :place", Rating.class);
        query.setParameter("user", user);
        query.setParameter("place", place);
        return query.uniqueResult();
    }
    
    public Rating findByUserAndImage(User user, PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        Query<Rating> query = session.createQuery("FROM Rating WHERE user = :user AND image = :image", Rating.class);
        query.setParameter("user", user);
        query.setParameter("image", image);
        return query.uniqueResult();
    }
    
    public Double getAverageRatingForPlace(Place place) {
        Session session = sessionFactory.getCurrentSession();
        Query<Double> query = session.createQuery("SELECT AVG(score) FROM Rating WHERE place = :place", Double.class);
        query.setParameter("place", place);
        return query.uniqueResult();
    }
    
    public Double getAverageRatingForImage(PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        Query<Double> query = session.createQuery("SELECT AVG(score) FROM Rating WHERE image = :image", Double.class);
        query.setParameter("image", image);
        return query.uniqueResult();
    }
}

