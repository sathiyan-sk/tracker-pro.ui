package com.trackerapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            String userEmail = (String) session.getAttribute("userEmail");
            String userRole = (String) session.getAttribute("userRole");
            
            if (userId != null && userEmail != null && userRole != null) {
                // Create authentication token
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userEmail, null, Arrays.asList(authority));
                
                // Set authentication in Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}