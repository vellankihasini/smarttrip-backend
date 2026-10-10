
package com.smarttrip.security;

import com.smarttrip.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository) {

        return email -> userRepository.findByEmail(email)
            .map(user ->
                org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities("USER")
                    .build()
            )
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + email)
            );
    }
}
