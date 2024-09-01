package com.hirehive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirehive.dto.UserDto;
import com.hirehive.model.User;
import com.hirehive.services.serviceImpl.EmailSendingService;
import com.hirehive.services.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("user") String userDtoJson) throws IOException {

        // Convert the JSON string to UserDto
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto = objectMapper.readValue(userDtoJson, UserDto.class);

        return userService.createUser(file, userDto);
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
