package com.lookbook.auth.infrastructure.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.aggregates.UserStatus;
import com.lookbook.user.domain.valueobjects.Username;

/**
 * Service that loads user details from the database for Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username
        User user = userRepository.findByUsername(Username.of(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Check if the user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("User is not active: " + username);
        }

        // Convert domain user to Spring Security UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername().getValue(),
                user.getHashedPassword(),
                // For now, every user has a default "USER" role
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}