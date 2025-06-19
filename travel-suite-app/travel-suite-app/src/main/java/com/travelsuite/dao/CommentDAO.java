package com.travelsuite.dao;

import com.travelsuite.model.Comment;
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
public class CommentDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public void save(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.save(comment);
    }
    
    public void update(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.update(comment);
    }
    
    public void delete(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(comment);
    }
    
    public Comment findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Comment.class, id);
    }
    
    public List<Comment> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Comment> query = session.createQuery("FROM Comment ORDER BY createdAt DESC", Comment.class);
        return query.list();
    }
    
    public List<Comment> findByPlace(Place place) {
        Session session = sessionFactory.getCurrentSession();
        Query<Comment> query = session.createQuery("FROM Comment WHERE place = :place ORDER BY createdAt DESC", Comment.class);
        query.setParameter("place", place);
        return query.list();
    }
    
    public List<Comment> findByImage(PlaceImage image) {
        Session session = sessionFactory.getCurrentSession();
        Query<Comment> query = session.createQuery("FROM Comment WHERE image = :image ORDER BY createdAt DESC", Comment.class);
        query.setParameter("image", image);
        return query.list();
    }
    
    public List<Comment> findByUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query<Comment> query = session.createQuery("FROM Comment WHERE user = :user ORDER BY createdAt DESC", Comment.class);
        query.setParameter("user", user);
        return query.list();
    }
}

