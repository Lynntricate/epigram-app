package com.lynn.epigramapp.service;

import com.lynn.epigramapp.dto.LoginDTO;
import com.lynn.epigramapp.dto.LoginResponse;
import com.lynn.epigramapp.dto.RegisterDTO;
import com.lynn.epigramapp.exception.*;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse register(RegisterDTO dto) {
        if (dto.username().length() < 3) {
            throw new InvalidUsernameException("Length should be > 2");
        }
        if (dto.password().length() < 8) {
            throw new InvalidPasswordException("Length should be > 7");
        }
        if (userRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException(dto.username());
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return new LoginResponse(token, savedUser.getUsername());
    }

    public LoginResponse login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UserNotFoundException(dto.username()));
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException(user.getUsername());
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getUsername());
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
}
