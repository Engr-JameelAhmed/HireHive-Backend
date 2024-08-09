package com.hirehive.controller;

import com.hirehive.dto.UserDto;
import com.hirehive.model.User;
import com.hirehive.services.serviceImpl.EmailSendingService;
import com.hirehive.services.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/newUser")
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.create(user);
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
