package com.graphql.chat.service;

import org.springframework.stereotype.Service;

import com.graphql.chat.model.User;
import com.graphql.chat.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User createUser(User user) {
        // basic check for uniqueness
        Optional<User> exists = repo.findByUsername(user.getUsername());
        if (exists.isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        return repo.save(user);
    }

    public Optional<User> findById(Long id) { return repo.findById(id); }
    public Optional<User> findByUsername(String username) { return repo.findByUsername(username); }
    
    public List<User> findAll() {
        return repo.findAll();
    }
}


