package com.travelsuite.service;

import com.travelsuite.dao.UserDAO;
import com.travelsuite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserDAO userDAO;
    
    public void saveUser(User user) {
        userDAO.save(user);
    }
    
    public void updateUser(User user) {
        userDAO.update(user);
    }
    
    public void deleteUser(User user) {
        userDAO.delete(user);
    }
    
    public User findUserById(Long id) {
        return userDAO.findById(id);
    }
    
    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    
    public User findUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }
    
    public List<User> findUsersByRole(User.UserRole role) {
        return userDAO.findByRole(role);
    }
    
    public boolean authenticateUser(String username, String password) {
        User user = userDAO.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
    
    public boolean isUsernameAvailable(String username) {
        return userDAO.findByUsername(username) == null;
    }
    
    public boolean isEmailAvailable(String email) {
        return userDAO.findByEmail(email) == null;
    }
    
    public User registerUser(String username, String password, String email, User.UserRole role) {
        if (!isUsernameAvailable(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (!isEmailAvailable(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(username, password, email, role);
        userDAO.save(user);
        return user;
    }
}

