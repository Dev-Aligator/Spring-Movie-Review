package com.dsi.spring.controller;

import com.dsi.spring.dao.RoleDao;
import com.dsi.spring.exception.ResourceAlreadyExists;
import com.dsi.spring.exception.ResourceNotFoundException;
import com.dsi.spring.model.User;
import com.dsi.spring.security.MyUserDetails;
import com.dsi.spring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleDao roleDao;

    @RequestMapping("/")
    public String home() {
        return "redirect:/movies";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "home/login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "home/login";
    }

    @RequestMapping("/logout-success")
    public String logoutSuccess() {
        return "home/login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "home/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupSubmit(User user, Model model) {
        try {
            authService.signupSubmit(user);
            return "home/login";
        } catch (ResourceAlreadyExists e) {
            model.addAttribute("signupError", true);
            return "home/signup";
        }
    }

    @RequestMapping("/all_user")
    public String allUser(Model model) {
        try {
            List<User> users = authService.allUser();
            model.addAttribute("allUser", users);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", true);
        }
        return "admin/user/all_user";
    }

    @RequestMapping("/delete/user/{id}")
    public String deleteUser(@PathVariable(value = "id") Long userId) {
        try {
            authService.deleteUser(userId);
        } catch (ResourceNotFoundException e) {
            System.out.println("Problem occur on Delete Function!");
        }
        return "redirect:/all_user";
    }

    @RequestMapping("/user_profile")
    public String profile(@AuthenticationPrincipal MyUserDetails principal, Model model) {
        try {
            User loggedUser = authService.profile(principal);
            model.addAttribute("user", loggedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home/profile";
    }

    @RequestMapping(value = "/edit/user/{id}", method = RequestMethod.GET)
    public String setEditUserPage(@PathVariable(value = "id") Long userId, Model model) {

        try {
            User user = authService.setEditUserPage(userId);
            model.addAttribute("user", user);
            model.addAttribute("allRole", roleDao.findAll());
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return "admin/user/edit_user";
    }

    @RequestMapping(value = "/edit/user/{id}", method = RequestMethod.POST)
    public String editUserSubmit(@PathVariable(value = "id") Long userId, User userDetails) {
        try {
            authService.editUserSubmit(userId, userDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/all_user";
    }

    @RequestMapping("/edit/user/profile/image/{id}")
    public String profilePictureUpdate(@PathVariable(value = "id") Long id, @RequestParam("file") MultipartFile file) {

        try {
            authService.saveUpdatedProfilePicture(id, file);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/user_profile";
    }
}

