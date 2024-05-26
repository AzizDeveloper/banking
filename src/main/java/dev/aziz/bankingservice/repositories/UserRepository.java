package dev.aziz.bankingservice.repositories;

import dev.aziz.bankingservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.phoneNumbers p " +
            "LEFT JOIN u.emails e " +
            "WHERE (:birthYear IS NULL OR EXTRACT(YEAR FROM u.birthDate) > :birthYear) " +
            "AND (:phone IS NULL OR p.number = :phone) " +
            "AND (:login IS NULL OR u.login LIKE CONCAT(:login, '%')) " +
            "AND (:email IS NULL OR e.name = :email)")
    Page<User> searchUsers(@Param("birthYear") Integer birthYear,
                           @Param("phone") String phone,
                           @Param("login") String login,
                           @Param("email") String email,
                           Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.phoneNumbers p " +
            "LEFT JOIN u.emails e " +
            "WHERE (:birthYear IS NULL OR EXTRACT(YEAR FROM u.birthDate) > :birthYear) " +
            "AND (:phone IS NULL OR p.number = :phone) " +
            "AND (:email IS NULL OR e.name = :email)")
    Page<User> searchUsersWithoutLogin(@Param("birthYear") Integer birthYear,
                                       @Param("phone") String phone,
                                       @Param("email") String email,
                                       Pageable pageable);

}
