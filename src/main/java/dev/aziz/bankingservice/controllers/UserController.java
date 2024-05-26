package dev.aziz.bankingservice.controllers;

import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.dtos.UserSummaryDto;
import dev.aziz.bankingservice.entities.User;
import dev.aziz.bankingservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/search")
    public Page<User> searchUsers(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Integer birthDate,
                                  @RequestParam(required = false) String phone,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "ASC") String sortDirection) {
        return userService.searchUsers(birthDate, phone, name, email, page, size, sortField, sortDirection);
    }

    @PatchMapping("/account")
    public ResponseEntity<UserSummaryDto> editAccount(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam BigDecimal money,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(userService.sendMoney(userDto, money, receiverId));
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

    @PostMapping("/phone-number")
    public ResponseEntity<UserSummaryDto> addPhoneNumberToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.addPhoneNumber(userDto, phoneNumber));
    }

    @PatchMapping("/phone-number")
    public ResponseEntity<UserSummaryDto> editPhoneNumberToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String oldPhoneNumber,
            @RequestParam String newPhoneNumber) {
        return ResponseEntity.ok(userService.editPhoneNumber(userDto, oldPhoneNumber, newPhoneNumber));
    }

    @DeleteMapping("/phone-number")
    public ResponseEntity<?> deletePhoneNumberFromUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.deletePhoneNumber(userDto, phoneNumber));
    }

}
