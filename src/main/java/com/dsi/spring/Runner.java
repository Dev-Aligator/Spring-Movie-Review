package com.dsi.spring;

import com.dsi.spring.dao.RoleDao;
import com.dsi.spring.dao.UserDao;
import com.dsi.spring.model.Role;
import com.dsi.spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public void run(String... args) throws Exception {

        if( roleDao.findAll().isEmpty() ) {

            // USER ROLE
            Role role_user = new Role();
            role_user.setName("USER");
            roleDao.save(role_user);

            // ADMIN ROLE
            Role role_admin = new Role();
            role_admin.setName("ADMIN");
            roleDao.save(role_admin);

            // super admin added
            User admin = new User();
            admin.setUsername("admin");
            admin.setEnabled(true);
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            admin.setEmail("admin@dsinnovator.com");
            admin.setProfilePicPath("/images/profile/default.png");

            Role role = roleDao.findByName("ADMIN"); // admin role fetched
            Set<Role> roles = new HashSet<Role>();
            roles.add(role);
            admin.setRoles(roles);
            userDao.save(admin);

        }

    }
}
