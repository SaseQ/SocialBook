package it.marczuk.socialbook.repository;

import it.marczuk.socialbook.model.Biography;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiographyReop extends JpaRepository<Biography, Long> {

    Optional<Biography> findByUser_Id(Long id);
}
