package com.dealersautocenter.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For demo purposes, using hardcoded user
        // In production, this should fetch from database
        if ("admin".equals(username)) {
            return new User("admin", 
                          passwordEncoder.encode("admin123"), 
                          new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}