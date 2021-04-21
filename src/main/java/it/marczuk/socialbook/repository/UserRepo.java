package it.marczuk.socialbook.repository;

import it.marczuk.socialbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findUsersByUsername(String username);

    Optional<User> findByEmail(String email);
}
