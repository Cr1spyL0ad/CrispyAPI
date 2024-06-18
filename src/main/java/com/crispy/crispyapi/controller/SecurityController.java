package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.dto.SignInRequest;
import com.crispy.crispyapi.dto.SignUpRequest;
import com.crispy.crispyapi.repository.UserRepository;
import com.crispy.crispyapi.security.JwtCore;
import com.crispy.crispyapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Зарегестрировать пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request(various errors, check message)"
                    )
            }
    )
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


    @Operation(
            summary = "Авторизовать пользователя(возвращает JWT)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED"
                    )
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
}
