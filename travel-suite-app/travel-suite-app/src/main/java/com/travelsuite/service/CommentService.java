package com.travelsuite.service;

import com.travelsuite.dao.CommentDAO;
import com.travelsuite.model.Comment;
import com.travelsuite.model.Place;
import com.travelsuite.model.PlaceImage;
import com.travelsuite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {
    
    @Autowired
    private CommentDAO commentDAO;
    
    public void saveComment(Comment comment) {
        commentDAO.save(comment);
    }
    
    public void updateComment(Comment comment) {
        commentDAO.update(comment);
    }
    
    public void deleteComment(Comment comment) {
        commentDAO.delete(comment);
    }
    
    public Comment findCommentById(Long id) {
        return commentDAO.findById(id);
    }
    
    public List<Comment> findAllComments() {
        return commentDAO.findAll();
    }
    
    public List<Comment> findCommentsByPlace(Place place) {
        return commentDAO.findByPlace(place);
    }
    
    public List<Comment> findCommentsByImage(PlaceImage image) {
        return commentDAO.findByImage(image);
    }
    
    public List<Comment> findCommentsByUser(User user) {
        return commentDAO.findByUser(user);
    }
    
    public Comment addCommentToPlace(User user, Place place, String content) {
        Comment comment = new Comment(content, user, place);
        commentDAO.save(comment);
        return comment;
    }
    
    public Comment addCommentToImage(User user, PlaceImage image, String content) {
        Comment comment = new Comment(content, user, image);
        commentDAO.save(comment);
        return comment;
    }
}

