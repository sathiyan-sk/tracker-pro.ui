package com.trackerapp.controller;

import com.trackerapp.dto.LoginRequest;
import com.trackerapp.dto.RegistrationRequest;
import com.trackerapp.model.User;
import com.trackerapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegistrationRequest request,
                                                       BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (bindingResult.hasErrors()) {
                response.put("success", false);
                response.put("message", "Validation errors");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check password confirmation
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                response.put("success", false);
                response.put("message", "Passwords do not match");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create user object
            User user = new User();
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setDepartment(request.getDepartment());
            user.setEmpId(request.getEmpId());
            user.setMobileNo(request.getMobileNo());
            
            // Register user
            User savedUser = userService.registerUser(user);
            
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request,
                                                    BindingResult bindingResult,
                                                    HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (bindingResult.hasErrors()) {
                response.put("success", false);
                response.put("message", "Invalid input");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<User> userOpt = userService.authenticateUser(request.getEmail(), request.getPassword());
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Store user in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole().toString());
                session.setAttribute("userName", user.getFullName());
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("redirectUrl", "/dashboard");
                response.put("user", Map.of(
                    "id", user.getId(),
                    "name", user.getFullName(),
                    "email", user.getEmail(),
                    "role", user.getRole().toString()
                ));
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        session.invalidate();
        
        response.put("success", true);
        response.put("message", "Logged out successfully");
        response.put("redirectUrl", "/");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userService.emailExists(email));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-empid")
    public ResponseEntity<Map<String, Boolean>> checkEmpId(@RequestParam String empId) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userService.empIdExists(empId));
        return ResponseEntity.ok(response);
    }
}