package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.dto.ChangePasswordRequest;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    @PutMapping("/users")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!Objects.equals(changePasswordRequest.getNewPassword(), changePasswordRequest.getNewPasswordConfirm()))
            return ResponseEntity.badRequest().body("Passwords aren't the same");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{name}")
    public ResponseEntity<?> changeName(@AuthenticationPrincipal User user, @PathVariable String name) {
        if(name.length() >= 4) {
            user.setName(name);
            userService.update(user);
            return ResponseEntity.ok("Name was successfully changed");
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user) {
        try {
            userService.delete(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
