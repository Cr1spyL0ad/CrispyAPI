package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.dto.SignInRequest;
import com.crispy.crispyapi.dto.SignUpRequest;
import com.crispy.crispyapi.repository.UserRepository;
import com.crispy.crispyapi.security.JwtCore;
import com.crispy.crispyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    @Autowired
    public SecurityController(UserRepository userRepository, @Lazy UserService userService, AuthenticationManager authenticationManager, JwtCore jwtCore) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        if(signUpRequest.getUsername().isEmpty() || signUpRequest.getName().isEmpty() || signUpRequest.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Field is empty");
        if(userRepository.existsByUsername(signUpRequest.getUsername()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This username already exists");
        if(userService.create(signUpRequest))
            return ResponseEntity.status(HttpStatus.CREATED).body("User was successfully created");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something goes wrong");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/user")
    public String getUser(Principal principal) {
        principal = SecurityContextHolder.getContext().getAuthentication();
        if(principal == null)
            return null;
        System.out.println(principal.toString());
        return principal.toString();
    }
}
