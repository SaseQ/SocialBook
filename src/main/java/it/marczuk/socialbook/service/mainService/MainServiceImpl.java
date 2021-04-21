package it.marczuk.socialbook.service.mainService;

import it.marczuk.socialbook.model.*;
import it.marczuk.socialbook.model.enums.Category;
import it.marczuk.socialbook.model.enums.Role;
import it.marczuk.socialbook.repository.*;
import it.marczuk.socialbook.service.mailService.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MainServiceImpl implements MainService {

    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final BiographyReop biographyReop;
    private final LikeRepo likeRepo;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public MainServiceImpl(UserRepo userRepo,
                           PostRepo postRepo,
                           BiographyReop biographyReop,
                           LikeRepo likeRepo,
                           TokenRepo tokenRepo,
                           PasswordEncoder passwordEncoder,
                           MailService mailService) {
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.biographyReop = biographyReop;
        this.likeRepo = likeRepo;
        this.tokenRepo = tokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    //userService
    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setAccountNonLocked(true);
        user.setCreateAccountDate(LocalDateTime.now());

        if(user.getGender().toString().equals("MALE")) {
            user.setUserImage("https://i.imgur.com/lURwGDr.png");
        } else {
            user.setUserImage("https://i.imgur.com/amYslVl.png");
        }

        sendToken(user);
        userRepo.save(user);
    }

    @Override
    public User getUserByUserId(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public List<User> getUsersByName(String username) {
        return userRepo.findUsersByUsername(username);
    }

    //biographyService
    @Override
    @Transactional
    public void editBiography(String biography, Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        Optional<Biography> biographyOptional = biographyReop.findByUser_Id(id);
        if(userOptional.isPresent()){
            if(biographyOptional.isPresent()) {
                Biography bio = biographyOptional.get();
                bio.setBio(biography);
                biographyReop.save(bio);
            } else {
                User user = userOptional.get();
                Biography bio = new Biography(biography, user);
                biographyReop.save(bio);
            }
        }
    }

    @Override
    public String getBiographyByUserId(Long id) {
        Optional<Biography> optionalBiography = biographyReop.findByUser_Id(id);
        String bio = "";
        if(optionalBiography.isPresent()) {
            bio = optionalBiography.get().getBio();
        }
        return bio;
    }

    //postService
    @Override
    public void addPost(Post post, Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            post.setCreateDate(LocalDateTime.now());
            post.setLikesCount(0);
            post.setUser(user);
            postRepo.save(post);
        }
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> allPosts = postRepo.findAll();
        Collections.reverse(allPosts);
        return allPosts;
//        return postRepo.findAll().stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsByUserId(Long id) {
        List<Post> userPosts = postRepo.findByUser_Id(id);
        Collections.reverse(userPosts);
        return userPosts;
//        return postRepo.findByUser_Id(id).stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsByCategory(Category category) {
        return postRepo.findByCategory(category);
    }

    @Override
    public List<Post> getPostsByTitle(String title) {
        return postRepo.findByTitle(title);
    }

    //likeService
    @Override
    @Transactional
    public void likeSystem(Long postId, Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        Optional<Post> postOptional = postRepo.findById(postId);
        if(postOptional.isPresent()) {
            Post post = postOptional.get();
            Optional<Like> likeOptional = likeRepo.findByUserIdAndPost(userId, post);
            if(userOptional.isPresent()) {
                if(likeOptional.isPresent()) {
                    post.setLikesCount(post.getLikesCount() - 1);
                    likeRepo.deleteByUserIdAndPost(userId, post);
                } else {
                    post.setLikesCount(post.getLikesCount() + 1);
                    likeRepo.save(new Like(userId, post));
                }
                postRepo.save(post);
            }
        }
    }

    //tokenService
    @Override
    public void confirmUserToken(String value) {
        Optional<Token> tokenOptional = tokenRepo.findByValue(value);
        if(tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            User user = token.getUser();
            user.setEnabled(true);
            userRepo.save(user);
        }
    }

    private void sendToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepo.save(token);

        String url = "http://localhost:8080/token?value=" + tokenValue;
        mailService.sendEmail(user.getEmail(), "SocialBook email confirmation", url);
    }
}
