package com.hirehive.services.serviceImpl;

import com.hirehive.constants.RoleType;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.User;
import com.hirehive.repository.UserRepository;
import com.hirehive.springSecurity.CustomUserDetails;
import com.hirehive.util.GlobalMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with this email Address : " + email));
//        if(user == null){
//            throw new RuntimeException("Wrong Credentials : "+email);
//        }
        return new CustomUserDetails(user);
    }


}
