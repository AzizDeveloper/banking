package dev.aziz.bankingservice.controllers;


import dev.aziz.bankingservice.config.UserAuthProvider;
import dev.aziz.bankingservice.dtos.CredentialsDto;
import dev.aziz.bankingservice.dtos.SignUpDto;
import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Tag(name = "Authentication endpoints")
public class AuthenticationController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @Operation(
            summary = "Login with username and password as JSON (CredentialsDto)."
    )
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user.getLogin()));
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Registering new user.",
            description = "Should be included all necessary fields as JSON (SignUpDto) body."
    )
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user.getLogin()));
        return ResponseEntity.created(URI.create("/users" + user.getId()))
                .body(user);
    }

    @Operation(
            summary = "User should be logged in."
    )
    @PostMapping("/signout")
    public ResponseEntity<Void> logOut(@AuthenticationPrincipal UserDto userDto) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

}
