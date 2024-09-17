package com.hirehive.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirehive.dto.JobDto;
import com.hirehive.dto.SearchJobsDTO;
import com.hirehive.dto.UserDto;
import com.hirehive.model.User;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.serviceImpl.EmailSendingService;
import com.hirehive.services.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("allUsers")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }
    @PostMapping(value = "/newUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto createUser(
            @RequestPart(value = "file", required = false) MultipartFile cv,
            @RequestPart(value = "file", required = false) MultipartFile proposal,
            @RequestPart("user") String userDtoJson) throws IOException {

        // Convert the JSON string to UserDto
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto = objectMapper.readValue(userDtoJson, UserDto.class);

        return userService.createUser(cv,proposal, userDto);
    }
    @PutMapping(value = "/update-user-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto updateUserCv(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        // Get the current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // Assuming the email is used as the username

        // Load the current user from the database using email
        UserDto currentUser = userService.getUserByEmail(currentEmail);

        // Update the user's CV with the provided file
        return userService.updateUserCv(file, currentUser);
    }
    @PutMapping(value = "/update-user-proposal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto updateUserProposal(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        // Get the current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // Assuming the email is used as the username

        // Load the current user from the database using email
        UserDto currentUser = userService.getUserByEmail(currentEmail);

        // Update the user's CV with the provided file
        return userService.updateUserProposal(file, currentUser);
    }
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDetails) {
        return userService.update(id, userDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }




}
