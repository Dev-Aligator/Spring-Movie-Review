package com.dsi.spring.service.impl;

import com.dsi.spring.dao.RoleDao;
import com.dsi.spring.dao.UserDao;
import com.dsi.spring.exception.ResourceAlreadyExists;
import com.dsi.spring.exception.ResourceNotFoundException;
import com.dsi.spring.model.Role;
import com.dsi.spring.model.User;
import com.dsi.spring.security.MyUserDetails;
import com.dsi.spring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.dsi.spring.utility.FileUpload;
import com.dsi.spring.utility.constants.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public void signupSubmit(User user) throws ResourceAlreadyExists {

        if (userDao.findByUsername(user.getUsername()) != null)
            throw new ResourceAlreadyExists("Username Already exists");

        // default role set as user
        Role role = roleDao.findByName("USER");
        Set<Role> roles = new HashSet<Role>();
        roles.add(role);
        user.setRoles(roles);
        user.setProfilePicPath("/images/profile/default.png");

        user.setEnabled(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword())); // password encrypted
        userDao.save(user);
    }

    @Override
    public List<User> allUser() throws ResourceNotFoundException {
        List<User> users = userDao.findAllUser();
        if (users.isEmpty())
            throw new ResourceNotFoundException("No User found");
        return users;
    }

    @Override
    public void deleteUser(Long userId) throws ResourceNotFoundException {

        userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userDao.deleteById(userId);
    }

    @Override
    public User profile(MyUserDetails principal) {
        User user = userDao.findById(principal.getId()).orElse(new User());
        return user;
    }

    @Override
    public User setEditUserPage(Long userId) throws ResourceNotFoundException {

        User user = userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user;
    }

    @Override
    public void editUserSubmit(Long userId, User userDetails) {

        User user = userDao.findById(userId).orElse(new User());
        user.setUsername(userDetails.getUsername());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setRoles(userDetails.getRoles());
        userDao.save(user);
    }

    @Override
    public void saveUpdatedProfilePicture(Long userId, MultipartFile file) throws ResourceNotFoundException {
        User user = userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String path = FileUpload.saveImage(ImageType.USER_PROFILE, user.getUsername(), file);
        user.setProfilePicPath(path);
        userDao.save(user);
    }

}
