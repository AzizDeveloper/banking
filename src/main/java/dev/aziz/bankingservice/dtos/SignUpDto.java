package dev.aziz.bankingservice.dtos;

import dev.aziz.bankingservice.entities.Email;
import dev.aziz.bankingservice.entities.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {

    private String firstName;
    private String lastName;
    private String login;
    private Integer birthYear;
    private String phoneNumber;
    private String email;
    private BigDecimal account;
    private char[] password;

}
