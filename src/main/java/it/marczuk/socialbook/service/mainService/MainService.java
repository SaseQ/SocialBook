package it.marczuk.socialbook.service.mainService;

import it.marczuk.socialbook.model.Token;
import it.marczuk.socialbook.model.enums.Category;
import it.marczuk.socialbook.model.Post;
import it.marczuk.socialbook.model.User;

import java.util.List;

public interface MainService {

    //userService
    void addUser(User user);

    User getUserByUserId(Long id);

    List<User> getUsersByName(String username);

    //biographyService
    void editBiography(String biography, Long id);

    String getBiographyByUserId(Long id);

    //postService
    void addPost(Post post, Long id);

    List<Post> getAllPosts();

    List<Post> getPostsByUserId(Long id);

    List<Post> getPostsByCategory(Category category);

    List<Post> getPostsByTitle(String title);

    //likeService
    void likeSystem(Long postId, Long userId);

    //tokenService
    void confirmUserToken(String value);
}
