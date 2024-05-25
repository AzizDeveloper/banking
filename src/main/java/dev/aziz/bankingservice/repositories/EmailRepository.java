package dev.aziz.bankingservice.repositories;

import dev.aziz.bankingservice.entities.Email;
import dev.aziz.bankingservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByName(String email);

    boolean existsEmailByName(String name);

    List<Email> findEmailsByUserId(Long id);

}
