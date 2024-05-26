package dev.aziz.bankingservice.dtos;

import dev.aziz.bankingservice.entities.Email;
import dev.aziz.bankingservice.entities.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private LocalDate birthDate;
    private BigDecimal account;
    private List<Email> emails;
    private List<PhoneNumber> phoneNumbers;
    private String token;

}
