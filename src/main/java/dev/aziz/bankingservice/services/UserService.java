package dev.aziz.bankingservice.services;

import dev.aziz.bankingservice.dtos.CredentialsDto;
import dev.aziz.bankingservice.dtos.SignUpDto;
import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.dtos.UserSummaryDto;
import dev.aziz.bankingservice.entities.Email;
import dev.aziz.bankingservice.entities.PhoneNumber;
import dev.aziz.bankingservice.entities.User;
import dev.aziz.bankingservice.exceptions.AppException;
import dev.aziz.bankingservice.mappers.UserMapper;
import dev.aziz.bankingservice.repositories.EmailRepository;
import dev.aziz.bankingservice.repositories.PhoneNumberRepository;
import dev.aziz.bankingservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final EmailRepository emailRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        log.info("User {} logged in.", credentialsDto.getLogin());
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public UserDto register(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByLogin(signUpDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }
        if (emailRepository.existsEmailByName(signUpDto.getEmail())) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        if (phoneNumberRepository.existsPhoneNumberByNumber(signUpDto.getPhoneNumber())) {
            throw new AppException("Phone number already exists", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .login(signUpDto.getLogin())
                .account(signUpDto.getAccount())
                .birthYear(signUpDto.getBirthYear())
                .build();
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())));
        User savedUser = userRepository.save(user);

        Email savedEmail = emailRepository.save(Email.builder().name(signUpDto.getEmail()).user(savedUser).build());
        PhoneNumber savedPhoneNumber = phoneNumberRepository.save(PhoneNumber.builder().number(signUpDto.getPhoneNumber()).user(savedUser).build());
        log.info("User by login {} has been registered.", savedUser.getLogin());
        UserDto userDto = userMapper.toUserDto(savedUser);
        userDto.setEmails(List.of(savedEmail));
        userDto.setPhoneNumbers(List.of(savedPhoneNumber));
        return userDto;
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDto> getAllUsers() {
        List<UserSummaryDto> userSummaryDtos =
                userMapper.usersToUserSummaryDtos(userRepository.findAll());
        for (UserSummaryDto userSummaryDto : userSummaryDtos) {
            List<Email> emailsByUserId = emailRepository.findEmailsByUserId(userSummaryDto.getId());
            userSummaryDto.setEmails(emailsByUserId);

            List<PhoneNumber> numbersByUserId = phoneNumberRepository.findPhoneNumbersByUserId(userSummaryDto.getId());
            userSummaryDto.setPhoneNumbers(numbersByUserId);
        }
        return userSummaryDtos;
    }

    public UserSummaryDto getOneUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return userMapper.userToUserSummaryDto(user);
    }

    @Transactional
    public UserSummaryDto addEmail(UserDto userDto, String email) {
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new AppException("User by " + userDto.getLogin() + "not found", HttpStatus.NOT_FOUND));
        Optional<Email> addingEmail = emailRepository.findByName(email);
        if (addingEmail.isPresent()) {
            throw new AppException("This Email already exists", HttpStatus.BAD_REQUEST);
        }
        List<Email> emails = user.getEmails();
        Email savedEmail = emailRepository.save(Email.builder().name(email).user(user).build());
        log.info("User by login {} has been added new email {}.", user.getLogin(), savedEmail.getName());
        emails.add(savedEmail);
        user.setEmails(emails);
        return userMapper.userToUserSummaryDto(user);
    }

    @Transactional
    public UserSummaryDto editEmail(UserDto userDto, String oldEmail, String newEmail) {
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new AppException("User by " + userDto.getLogin() + "not found", HttpStatus.NOT_FOUND));
        Email oldDbEmail = emailRepository.findByName(oldEmail)
                .orElseThrow(() -> new AppException("Email not found", HttpStatus.NOT_FOUND));
        Optional<Email> newOptionalEmail = emailRepository.findByName(newEmail);
        if (newOptionalEmail.isPresent()) {
            throw new AppException("This Email already exists", HttpStatus.BAD_REQUEST);
        }
        user.getEmails().remove(oldDbEmail);
        oldDbEmail.setName(newEmail);
        Email savedNewEmail = emailRepository.save(oldDbEmail);
        log.info("User by login {} has been updated email {}.", user.getLogin(), savedNewEmail.getName());
        System.out.println("user.getEmails() = " + user.getEmails());
        user.getEmails().add(savedNewEmail);
        User savedUser = userRepository.save(user);
        System.out.println("user.getEmails() = " + user.getEmails());
        return userMapper.userToUserSummaryDto(savedUser);
    }

    //todo: complete this method. user must have at least one email, not 0.
    @Transactional
    public String deleteEmail(UserDto userDto, String email) {
        log.info("User {} deleted email {}.", userDto.getLogin(), email);
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new AppException("User by " + userDto.getLogin() + "not found", HttpStatus.NOT_FOUND));
        if (user.getEmails().size() <= 1) {
            throw new AppException("Users cannot delete all their emails", HttpStatus.BAD_REQUEST);
        }
        Email deletingEmail = emailRepository.findByName(email)
                .orElseThrow(() -> new AppException("This Email does not exist", HttpStatus.NOT_FOUND));
        user.getEmails().remove(deletingEmail);
        userRepository.save(user);
        emailRepository.delete(deletingEmail);
        return "Email by name: " + email + " deleted successfully.";
    }

    //todo: complete this method
    @Transactional
    public UserSummaryDto addPhoneNumber(UserDto userDto, String phoneNumber) {
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new AppException("User by " + userDto.getLogin() + "not found", HttpStatus.NOT_FOUND));
        Optional<PhoneNumber> addingPhoneNumber = phoneNumberRepository.findByNumber(phoneNumber);
        if (addingPhoneNumber.isPresent()) {
            throw new AppException("This phone number already exists", HttpStatus.BAD_REQUEST);
        }
        List<PhoneNumber> phoneNumbers = user.getPhoneNumbers();
        PhoneNumber savedPhoneNumber = phoneNumberRepository.save(PhoneNumber.builder().number(phoneNumber).user(user).build());
        log.info("User by login {} has been added new phone number {}.", user.getLogin(), savedPhoneNumber.getNumber());
        phoneNumbers.add(savedPhoneNumber);
        user.setPhoneNumbers(phoneNumbers);
        return userMapper.userToUserSummaryDto(user);
    }

    //todo: complete this method. user must have at least one phone number, not 0.
    @Transactional
    public String deletePhoneNumber(UserDto userDto, String phoneNumber) {
        log.info("User {} deleted phone number {}.", userDto.getLogin(), phoneNumber);
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new AppException("User by " + userDto.getLogin() + "not found", HttpStatus.NOT_FOUND));
        if (user.getPhoneNumbers().size() <= 1) {
            throw new AppException("Users cannot delete all their phone numbers", HttpStatus.BAD_REQUEST);
        }
        PhoneNumber deletingPhoneNumber = phoneNumberRepository.findByNumber(phoneNumber)
                .orElseThrow(() -> new AppException("This phone number does not exist", HttpStatus.NOT_FOUND));
        user.getPhoneNumbers().remove(deletingPhoneNumber);
        userRepository.save(user);
        phoneNumberRepository.delete(deletingPhoneNumber);
        return "Phone number: " + phoneNumber + " deleted successfully.";
    }


}
