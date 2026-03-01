package com.lynn.epigramapp.service;

import com.lynn.epigramapp.dto.LoginDTO;
import com.lynn.epigramapp.dto.RegisterDTO;
import com.lynn.epigramapp.exception.*;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    UserService userService;

    private RegisterDTO validRegister;
    private LoginDTO validLogin;

    @BeforeEach
    void setUp() {
        validRegister = new RegisterDTO("Ana", "password123");
        validLogin = new LoginDTO("Ana", "password123");
    }

    @Test
    void registerSuccessful() {
        when(userRepository.existsByUsername("Ana")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");

        User savedUser = new User();
        savedUser.setUsername("Ana");
        savedUser.setPassword("encodedPass");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("jwt-token");

        var response = userService.register(validRegister);

        assertEquals("Ana", response.username());
        assertEquals("jwt-token", response.token());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerInvalidUsernameFails() {
        RegisterDTO dto = new RegisterDTO("ab", "password123");
        assertThrows(InvalidUsernameException.class, () -> userService.register(dto));
    }

    @Test
    void registerInvalidPasswordFails() {
        RegisterDTO dto = new RegisterDTO("Ana", "pass");
        assertThrows(InvalidPasswordException.class, () -> userService.register(dto));
    }

    @Test
    void registerUsernameAlreadyExistsFails() {
        when(userRepository.existsByUsername("Ana")).thenReturn(true);
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.register(validRegister));
    }

    @Test
    void loginCorrectDataSucceeds() {
        User user = new User();
        user.setUsername("Ana");
        user.setPassword("encodedPass");
        when(userRepository.findByUsername("Ana")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        var response = userService.login(validLogin);

        assertEquals("Ana", response.username());
        assertEquals("jwt-token", response.token());
    }

    @Test
    void loginUsernameNotFoundFails() {
        when(userRepository.findByUsername("Ana")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.login(validLogin));
    }

    @Test
    void loginInvalidPasswordFails() {
        User user = new User();
        user.setUsername("Ana");
        user.setPassword("encodedPass");
        when(userRepository.findByUsername("Ana")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(validLogin));
    }

    @Test
    void findByUsernameSuccessful() {
        User user = new User();
        user.setUsername("Ana");
        when(userRepository.findByUsername("Ana")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("Ana");
        assertEquals("Ana", result.getUsername());
    }

    @Test
    void findByUsernameNotFound() {
        when(userRepository.findByUsername("Ana")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.findByUsername("Ana"));
    }

}