package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.dto.ChangePasswordRequest;
import com.crispy.crispyapi.dto.UserDto;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "Возвращает информацию по пользователю с указанным id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content =
            @Content(schema = @Schema(type = "object", implementation = UserDto.class))),

            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.convertToDto(userService.read(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @Operation(summary = "Возвращает информацию по текущему пользователю", responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content =
                    @Content(schema = @Schema(type = "object", implementation = UserDto.class))),

                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    @GetMapping("/users")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(userService.convertToDto(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }



    @Operation(summary = "Сменить пароль для текущего пользователя", description = "Пароли в теле запроса должны совпадать", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/users")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!Objects.equals(changePasswordRequest.getNewPassword(), changePasswordRequest.getNewPasswordConfirm()))
            return ResponseEntity.badRequest().body("Passwords aren't the same");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Сменить имя для текущего пользователя", description = "минимум 4 символа", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/users")
    public ResponseEntity<?> changeName(@AuthenticationPrincipal User user, @RequestBody String name) {
        if(name.length() >= 4) {
            user.setName(name);
            userService.update(user);
            return ResponseEntity.ok("Name was successfully changed");
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Удалить текущего пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
