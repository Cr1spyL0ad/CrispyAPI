package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.repository.UserRepository;
import com.crispy.crispyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if(!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User with id %d does not exist", id));
        if(userService.update(user, id))
            return ResponseEntity.ok("Success");
        return ResponseEntity.badRequest().body("Something goes wrong");
    }
}
