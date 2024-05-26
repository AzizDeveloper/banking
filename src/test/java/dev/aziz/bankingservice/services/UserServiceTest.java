package dev.aziz.bankingservice.services;

import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.dtos.UserSummaryDto;
import dev.aziz.bankingservice.entities.User;
import dev.aziz.bankingservice.exceptions.AppException;
import dev.aziz.bankingservice.mappers.UserMapper;
import dev.aziz.bankingservice.mappers.UserMapperImpl;
import dev.aziz.bankingservice.repositories.EmailRepository;
import dev.aziz.bankingservice.repositories.PhoneNumberRepository;
import dev.aziz.bankingservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void sendMoneyTest() {
        // given
        User sender = User.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov")
                .birthDate(LocalDate.parse("2000-01-26")).initialDeposit(BigDecimal.valueOf(1200)).account(BigDecimal.valueOf(1300)).build();
        User receiver = User.builder().id(2L).login("azimdev").firstName("Azim").lastName("Abdukarimov")
                .birthDate(LocalDate.parse("1996-01-26")).initialDeposit(BigDecimal.valueOf(5000)).account(BigDecimal.valueOf(5000)).build();
        UserDto userDto = userMapper.toUserDto(sender);
        BigDecimal money = BigDecimal.valueOf(100);

        // when
        when(userRepository.findByLogin(sender.getLogin())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        UserSummaryDto userSummaryDto = userService.sendMoney(userDto, money, receiver.getId());

        // then
        assertAll(() -> {
            assertEquals(new BigDecimal(5100), receiver.getAccount());
            assertEquals(new BigDecimal(1200), sender.getAccount());
        });

        verify(userRepository, times(1)).save(sender);
        verify(userRepository, times(1)).save(receiver);
    }

    @Test
    void sendMoneyNotEnoughFundsTest() {
        // given
        User sender = User.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov")
                .birthDate(LocalDate.parse("2000-01-26")).initialDeposit(BigDecimal.valueOf(1200)).account(BigDecimal.valueOf(50)).build();
        User receiver = User.builder().id(2L).login("azimdev").firstName("Azim").lastName("Abdukarimov")
                .birthDate(LocalDate.parse("1996-01-26")).initialDeposit(BigDecimal.valueOf(5000)).account(BigDecimal.valueOf(5000)).build();
        UserDto userDto = userMapper.toUserDto(sender);
        BigDecimal money = BigDecimal.valueOf(100);

        // when
        when(userRepository.findByLogin(sender.getLogin())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        AppException exception = assertThrows(AppException.class, () -> {
            userService.sendMoney(userDto, money, receiver.getId());
        });

        // then
        assertEquals("You do not have enough money.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void sendMoneySenderNotFoundTest() {
        // given
        UserDto userDto = UserDto.builder().login("azizdev").build();
        BigDecimal money = BigDecimal.valueOf(100);
        Long receiverId = 2L;

        // when
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            userService.sendMoney(userDto, money, receiverId);
        });

        // then
        assertEquals("User by azizdev not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void sendMoneyReceiverNotFoundTest() {
        // given
        User sender = User.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov")
                .birthDate(LocalDate.parse("2000-01-26")).initialDeposit(BigDecimal.valueOf(1200)).account(BigDecimal.valueOf(1300)).build();
        UserDto userDto = userMapper.toUserDto(sender);
        BigDecimal money = BigDecimal.valueOf(100);
        Long receiverId = 2L;

        // when
        when(userRepository.findByLogin(sender.getLogin())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            userService.sendMoney(userDto, money, receiverId);
        });

        // then
        assertEquals("User by id 2 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}


