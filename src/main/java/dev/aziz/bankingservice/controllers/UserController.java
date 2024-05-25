package dev.aziz.bankingservice.controllers;

import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.dtos.UserSummaryDto;
import dev.aziz.bankingservice.entities.User;
import dev.aziz.bankingservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummaryDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getOneUserById(id));
    }

    @PostMapping("/email")
    public ResponseEntity<UserSummaryDto> addEmailToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String email) {
        return ResponseEntity.ok(userService.addEmail(userDto, email));
    }

    @PatchMapping("/email")
    public ResponseEntity<UserSummaryDto> editEmail(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String oldEmail,
            @RequestParam String newEmail) {
        return ResponseEntity.ok(userService.editEmail(userDto, oldEmail, newEmail));
    }

    @DeleteMapping("/email")
    public ResponseEntity<?> deleteEmailFromUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String email) {
        return ResponseEntity.ok(userService.deleteEmail(userDto, email));
    }

    @PatchMapping("/phone-number")
    public ResponseEntity<UserSummaryDto> addPhoneNumberToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.addPhoneNumber(userDto, phoneNumber));
    }

    @DeleteMapping("/phone-number")
    public ResponseEntity<?> deletePhoneNumberFromUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.deletePhoneNumber(userDto, phoneNumber));
    }

    //todo: add phoneNumber
    //todo: delete phoneNumber and email, should be left at least one last one

    //todo: edit existing phoneNumber

    //todo: send money to another user
    //todo: users account cannot go to minus


}