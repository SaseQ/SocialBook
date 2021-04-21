package it.marczuk.socialbook.repository;

import it.marczuk.socialbook.model.Like;
import it.marczuk.socialbook.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long>  {

    Optional<Like> findByUserIdAndPost(Long id, Post post);

    void deleteByUserIdAndPost(Long id, Post post);
}
