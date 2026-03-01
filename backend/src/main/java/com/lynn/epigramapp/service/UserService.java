package com.lynn.epigramapp.service;

import com.lynn.epigramapp.exception.InvalidCredentialsException;
import com.lynn.epigramapp.exception.UserNotFoundException;
import com.lynn.epigramapp.exception.UsernameAlreadyExistsException;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof ConstraintViolationException cve) {
                // Assumes username is the only custom column in users that has a UNIQUE constraint
                throw new UsernameAlreadyExistsException(user.getUsername());
            }
            throw exception; // Rethrow in case of other integrity violations
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found with username: " + username
        ));
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(user.getUsername());
        }
        return user;
    }
}
