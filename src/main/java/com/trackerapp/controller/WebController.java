package com.trackerapp.controller;

import com.trackerapp.dto.RegistrationRequest;
import com.trackerapp.dto.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "loginPage";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/success")
    public String success() {
        return "success";
    }
    
    @GetMapping("/userlogin")
    public String userLogin() {
        return "userlogin";
    }
    
    @GetMapping("/forget")
    public String forgotPassword() {
        return "forget";
    }
}