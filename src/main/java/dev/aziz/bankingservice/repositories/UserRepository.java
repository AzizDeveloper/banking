package dev.aziz.bankingservice.repositories;

import dev.aziz.bankingservice.dtos.UserDto;
import dev.aziz.bankingservice.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

//    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.emails")
//    @Query(nativeQuery = true,
//            value = "SELECT u.*, e.*, p.* FROM app_user u " +
//                    "LEFT JOIN email e ON u.id = e.user_id " +
//                    "LEFT JOIN phone_number p ON u.id = p.user_id;"
//    )
//    List<User> findAllUsersWithEmails();

//    @EntityGraph(attributePaths = {"emails", "phoneNumbers"})
//    List<User> findAll();


//    List<User> findAllByWithEmailsAndWithPhoneNumbers();
}
