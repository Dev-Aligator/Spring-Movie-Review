package com.dsi.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder()); // encrypted password
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/login", "/signup", "/signup-submit", "/images/**", "/css/**", "/js/**").permitAll()
                .antMatchers("/", "/user_profile", "/all_user").hasAnyAuthority("USER", "ADMIN").antMatchers("/new/**")
                .hasAnyAuthority("USER", "ADMIN").antMatchers("/edit/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/delete/**").hasAnyAuthority("ADMIN").anyRequest().authenticated().and().formLogin()
                .loginPage("/login").permitAll().failureUrl("/login-error").and().logout().invalidateHttpSession(true)
                .clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout-success").permitAll();
    }

}
