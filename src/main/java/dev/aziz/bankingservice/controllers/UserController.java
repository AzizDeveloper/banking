package dev.aziz.bankingservice.controllers;

import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.dtos.UserSummaryDto;
import dev.aziz.bankingservice.entities.User;
import dev.aziz.bankingservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users endpoints")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users and filter and search.",
            description = "String name, Integer birthDate, String phone, String email, int page, int size, String sortField, String sortDirection these params can be added to request."
    )
    @GetMapping
    public List<UserSummaryDto> searchUsers(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) Integer birthDate,
                                            @RequestParam(required = false) String phone,
                                            @RequestParam(required = false) String email,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortField,
                                            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return userService.searchUsers(birthDate, phone, name, email, page, size, sortField, sortDirection);
    }

    @Operation(
            summary = "Get a user by id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserSummaryDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getOneUserById(id));
    }

    @Operation(
            summary = "Send money endpoint.",
            description = "Wanted money and receiverId should be added in param."
    )
    @PatchMapping("/account")
    public ResponseEntity<UserSummaryDto> editAccount(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam BigDecimal money,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(userService.sendMoney(userDto, money, receiverId));
    }

    @Operation(
            summary = "Add email.",
            description = "Adds email to signed user. Email should be added to param."
    )
    @PostMapping("/email")
    public ResponseEntity<UserSummaryDto> addEmailToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String email) {
        return ResponseEntity.ok(userService.addEmail(userDto, email));
    }

    @Operation(
            summary = "Edit email.",
            description = "Edits email to signed user. Old email and new email should be added to param."
    )
    @PatchMapping("/email")
    public ResponseEntity<UserSummaryDto> editEmail(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String oldEmail,
            @RequestParam String newEmail) {
        return ResponseEntity.ok(userService.editEmail(userDto, oldEmail, newEmail));
    }

    @Operation(
            summary = "Delete email.",
            description = "Email should be added to param."
    )
    @DeleteMapping("/email")
    public ResponseEntity<?> deleteEmailFromUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String email) {
        return ResponseEntity.ok(userService.deleteEmail(userDto, email));
    }

    @Operation(
            summary = "Add phone number.",
            description = "Adds phone number to signed user. Phone number should be added to param."
    )
    @PostMapping("/phone-number")
    public ResponseEntity<UserSummaryDto> addPhoneNumberToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.addPhoneNumber(userDto, phoneNumber));
    }

    @Operation(
            summary = "Edit phone number.",
            description = "Edits phone number to signed user. Old phone number and new phone number should be added to param."
    )
    @PatchMapping("/phone-number")
    public ResponseEntity<UserSummaryDto> editPhoneNumberToUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String oldPhoneNumber,
            @RequestParam String newPhoneNumber) {
        return ResponseEntity.ok(userService.editPhoneNumber(userDto, oldPhoneNumber, newPhoneNumber));
    }

    @Operation(
            summary = "Delete phone number.",
            description = "Phone number should be added to param."
    )
    @DeleteMapping("/phone-number")
    public ResponseEntity<?> deletePhoneNumberFromUser(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.deletePhoneNumber(userDto, phoneNumber));
    }

}
