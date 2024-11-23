package com.jatin.online_banking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jatin.online_banking.dao.UserRegisterDao;
import com.jatin.online_banking.exception.ResourceNotFoundException;
import com.jatin.online_banking.model.AuthenticationRequest;
import com.jatin.online_banking.model.AuthenticationResponse;
import com.jatin.online_banking.model.User;
import com.jatin.online_banking.model.UserRole;
import com.jatin.online_banking.model.VerifyTokenResponse;
import com.jatin.online_banking.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Register a new user by encoding their password and saving them in the
     * database.
     *
     * @param userRegisterDao The user to register.
     * @return The registered user.
     */
    public User register(UserRegisterDao userRegisterDao) {
        User newUser = new User();
        newUser.setName(userRegisterDao.getName());
        newUser.setPassword(userRegisterDao.getPassword());
        newUser.setRole(UserRole.USER);
        newUser.setEmail(userRegisterDao.getEmail());
        newUser.setAddress(userRegisterDao.getAddress());
        newUser.setPhoneNumber(userRegisterDao.getPhoneNumber());

        newUser.setPassword(encoder.encode(userRegisterDao.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    /**
     * Verify a user's credentials, generate a JWT token, and return authentication
     * response.
     *
     * @param authenticationRequest The user's authentication request containing
     *                              email and password.
     * @return An AuthenticationResponse containing the JWT token and user details.
     */
    public AuthenticationResponse verify(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        
        String token = jwtService.generateToken(user.getEmail());

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setUser(user);
        return response;
    }

    /**
     * Retrieve all users in the system.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Validate a JWT token and retrieve the associated user details.
     *
     * @param token The JWT token to validate.
     * @return A VerifyTokenResponse containing the token and user ID.
     */
    public VerifyTokenResponse validateToken(String token) {
        String email = jwtService.extractUserName(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new ResourceNotFoundException("Invalid token: User not found");
        }

        VerifyTokenResponse response = new VerifyTokenResponse();
        response.setToken(token);
        response.setUserId(user.getUserId());
        return response;
    }

    /**
     * Delete user by userId
     *
     * @param userId
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
