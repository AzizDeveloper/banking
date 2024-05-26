package dev.aziz.bankingservice.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Firstname should not be empty")
    private String firstName;

    @NotNull(message = "Lastname should not be empty")
    private String lastName;

    @NotNull(message = "Birth of year should not be empty")
    private Integer birthYear;

    @NotNull(message = "Login should not be empty")
    @Column(unique = true)
    private String login;

    @NotNull(message = "Password should not be empty")
    private String password;

    @Column(precision = 8, scale = 3)
    @Positive
    private BigDecimal account;

    @Column(precision = 8, scale = 3)
    @Positive
    private BigDecimal initialDeposit;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNumber> phoneNumbers;

    @JsonManagedReference
    public List<Email> getEmails() {
        return emails;
    }

    @JsonManagedReference
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }
}
