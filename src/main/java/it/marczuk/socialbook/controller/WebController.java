package it.marczuk.socialbook.controller;

import it.marczuk.socialbook.model.Biography;
import it.marczuk.socialbook.model.enums.Category;
import it.marczuk.socialbook.model.Post;
import it.marczuk.socialbook.model.User;
import it.marczuk.socialbook.service.mainService.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class WebController {

    private final MainService service;

    @Autowired
    public WebController(MainService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getMainPage(Model modal) {
        modal.addAttribute("userInfo", getUserContext());
        modal.addAttribute("newPost", new Post());
        modal.addAttribute("allPosts", service.getAllPosts());
        return "index";
    }

    @GetMapping("/sort")
    public String getMainSortPage(@RequestParam Map<String,String> requestParams, Model modal) {
        String categoryString = requestParams.get("category");
        String title = requestParams.get("title");

        modal.addAttribute("userInfo", getUserContext());
        modal.addAttribute("newPost", new Post());
        if(title == null) {
            modal.addAttribute("allPosts", service.getPostsByCategory(Category.valueOf(categoryString)));
        } else {
            modal.addAttribute("allUsers", service.getUsersByName(title));
            modal.addAttribute("allPosts", service.getPostsByTitle(title));
        }
        return "index";
    }

    @GetMapping("/profile")
    public String getUserProfilePage(@RequestParam(value = "id", required = false) Long userId, Model modal) {
        if(userId == null) {
            modal.addAttribute("myProfile", "true");
            modal.addAttribute("userInfo", getUserContext());
            modal.addAttribute("userBio", service.getBiographyByUserId(getUserContext().getId()));
            modal.addAttribute("allUserPosts", service.getPostsByUserId(getUserContext().getId()));
        } else {
            modal.addAttribute("userInfo", service.getUserByUserId(userId));
            modal.addAttribute("userBio", service.getBiographyByUserId(userId));
            modal.addAttribute("allUserPosts", service.getPostsByUserId(userId));
        }
        modal.addAttribute("biography", new Biography());
        modal.addAttribute("newPost", new Post());
        return "profile";
    }

    @PostMapping("/editbio")
    public String editBio(@RequestParam(value = "biography") String biography) {
        service.editBiography(biography, getUserContext().getId());
        return "redirect:/profile";
    }

    @PostMapping("/addpost/{site}")
    public String addPost(@ModelAttribute Post post, @PathVariable String site) {
        service.addPost(post, getUserContext().getId());
        return "redirect:/" + site;
    }

    @GetMapping("/like/{site}/{id}")
    public String likePost(@PathVariable String site, @PathVariable Long id) {
        service.likeSystem(id, getUserContext().getId());
        return "redirect:/" + site;
    }

    @GetMapping("/singin")
    public String getLoginPage(){
        return "singin";
    }

    @GetMapping("/singup")
    public String getRegisterPage(Model modal){
        modal.addAttribute("user", new User());
        return "singup";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        service.addUser(user);
        return "redirect:/singin";
    }

    @GetMapping("/token")
    public String tokenConfirm(@RequestParam(value = "value") String value) {
        service.confirmUserToken(value);
        return "redirect:/singin";
    }

    private User getUserContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
