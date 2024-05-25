package dev.aziz.bankingservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "phone_number")
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@EqualsAndHashCode(exclude = {"user"})
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO: this regex should be rewritten
    @NotNull(message = "Phone number should not be empty")
//    @Pattern(regexp = "String regex = \"^[0-9]+$\";\n")
//    @Pattern(regexp = "String regex = ^(?:\\+?\\d{1,3})?[1-9]\\d{9}$\n")
    @Column(unique = true)
    private String number;

//    @JsonBackReference
//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    public User getUser() {
        return user;
    }
}
