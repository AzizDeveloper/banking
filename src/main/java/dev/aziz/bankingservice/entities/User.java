package dev.aziz.bankingservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@EqualsAndHashCode(exclude = {"emails", "phoneNumbers"})
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

//    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails;

//    @JsonManagedReference
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
