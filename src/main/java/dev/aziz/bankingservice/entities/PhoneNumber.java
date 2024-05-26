package dev.aziz.bankingservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "phone_number")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Phone number should not be empty")
    @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "Invalid phone number format")
    @Column(unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    public User getUser() {
        return user;
    }
}
