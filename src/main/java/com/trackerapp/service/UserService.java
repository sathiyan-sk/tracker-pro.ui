package com.trackerapp.service;

import com.trackerapp.model.User;
import com.trackerapp.model.Role;
import com.trackerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Check if employee ID already exists
        if (userRepository.existsByEmpId(user.getEmpId())) {
            throw new RuntimeException("Employee ID already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role as USER
        user.setRole(Role.USER);
        
        return userRepository.save(user);
    }
    
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean empIdExists(String empId) {
        return userRepository.existsByEmpId(empId);
    }
    
    public void createDefaultAdmin() {
        if (!userRepository.existsByEmail("admin@trackerpro.com")) {
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setEmail("admin@trackerpro.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setDepartment("Administration");
            admin.setEmpId("ADMIN001");
            admin.setMobileNo("+91-9999999999");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            
            userRepository.save(admin);
        }
    }
}