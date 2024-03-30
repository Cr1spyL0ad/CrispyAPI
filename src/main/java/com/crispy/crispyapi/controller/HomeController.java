package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class HomeController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public HomeController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.convertToDto(userService.read(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
    @GetMapping("/users")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(userService.convertToDto(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PatchMapping("/users")
    public ResponseEntity<?> patchUser(@AuthenticationPrincipal User user, @RequestBody User userFromRequest) {
        if(userFromRequest.getName() != null) {
            user.setName(userFromRequest.getName());
            userService.update(user);
            return ResponseEntity.ok("Name was successfully changed");
        }
        if(userFromRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userFromRequest.getPassword()));
            userService.update(user);
            return ResponseEntity.ok("Password was successfully changed");
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user) {
        try {
            userService.delete(user.getId());
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
