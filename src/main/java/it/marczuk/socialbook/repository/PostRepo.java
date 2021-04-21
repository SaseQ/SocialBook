package it.marczuk.socialbook.repository;

import it.marczuk.socialbook.model.enums.Category;
import it.marczuk.socialbook.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findByUser_Id(Long id);

    List<Post> findByCategory(Category category);

    List<Post> findByTitle(String title);
}
