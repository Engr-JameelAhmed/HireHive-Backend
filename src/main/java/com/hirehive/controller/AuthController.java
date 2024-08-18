package com.hirehive.controller;

import com.hirehive.dto.AuthResponseDto;
import com.hirehive.dto.LoginDto;
import com.hirehive.services.AuthService;
import com.hirehive.services.serviceImpl.CustomUserDetailsService;
import com.hirehive.springSecurity.CustomUserDetails;
import com.hirehive.springSecurity.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService myUserDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> createAuthenticationToken(@RequestBody LoginDto loginCredentials) {
        try {
            // Load user by email
            UserDetails userDetails = myUserDetailService.loadUserByUsername(loginCredentials.getEmail());

            // Check if the password matches
            if (passwordEncoder.matches(loginCredentials.getPassword(), userDetails.getPassword())) {
                // Generate JWT token if the password is correct
                String jwtToken = jwtTokenProvider.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponseDto(jwtToken));
            } else {
                // Return response indicating that the password is incorrect
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDto("Incorrect password"));
            }
        } catch (UsernameNotFoundException ex) {
            // Handle case where email does not exist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDto("Invalid email or password"));
        }
    }

}
