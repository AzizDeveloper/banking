package dev.aziz.bankingservice.repositories;

import dev.aziz.bankingservice.entities.Email;
import dev.aziz.bankingservice.entities.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    Optional<PhoneNumber> findByNumber(String number);

    boolean existsPhoneNumberByNumber(String number);

    List<PhoneNumber> findPhoneNumbersByUserId(Long id);
}
