package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/main")
public class HomeController {
    private final UserService userService;
    public HomeController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.convertToDto(user));
    }

}
