package dev.aziz.bankingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {

    private String firstName;
    private String lastName;
    private String login;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private BigDecimal account;
    private char[] password;

}
