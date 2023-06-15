package com.dsi.spring.service;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.spring.model.User;
import com.dsi.spring.dao.*;
import com.dsi.spring.model.Role;
import com.dsi.spring.exception.*;;
@Service
public class UserService {
 
    @Autowired
    private UserDao userDao;
    private RoleDao roleDao;
    public void processOAuthPostLogin(String username) {

        User user = new User();
        if (userDao.findByUsername(user.getUsername()) != null)
            System.out.print("Hmi");
        else{
        // default role set as user
        Role role = roleDao.findByName("USER");
        Set<Role> roles = new HashSet<Role>();
        roles.add(role);
        user.setRoles(roles);
        user.setProfilePicPath("/images/profile/default.png");

        user.setEnabled(true);
        user.setPassword("Hmi hmu"); // password encrypted
        userDao.save(user);
        }
         
    }
     
}