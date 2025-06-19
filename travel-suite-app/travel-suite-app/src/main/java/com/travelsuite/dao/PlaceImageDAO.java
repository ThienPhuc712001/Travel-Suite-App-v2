package com.travelsuite.dao;

import com.travelsuite.model.Place;
import com.travelsuite.model.PlaceImage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceImageDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public void save(PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        session.save(image);
    }
    
    public void update(PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        session.update(image);
    }
    
    public void delete(PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(image);
    }
    
    public PlaceImage findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(PlaceImage.class, id);
    }
    
    public List<PlaceImage> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<PlaceImage> query = session.createQuery("FROM PlaceImage ORDER BY uploadedAt DESC", PlaceImage.class);
        return query.list();
    }
    
    public List<PlaceImage> findByPlace(Place place) {
        Session session = sessionFactory.getCurrentSession();
        Query<PlaceImage> query = session.createQuery("FROM PlaceImage WHERE place = :place ORDER BY uploadedAt DESC", PlaceImage.class);
        query.setParameter("place", place);
        return query.list();
    }
    
    public List<PlaceImage> searchByDescription(String keyword) {
        Session session = sessionFactory.getCurrentSession();
        Query<PlaceImage> query = session.createQuery("FROM PlaceImage WHERE description LIKE :keyword ORDER BY uploadedAt DESC", PlaceImage.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.list();
    }
}

