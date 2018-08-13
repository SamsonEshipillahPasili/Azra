package com.azra.configuration;

import com.azra.services.AzraUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    // password encryptor/decryptor.
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private AzraUserDetailsService userDetailsService;

    /* Configure Authentication of Azra users */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // the userDetailsService loads a user entity given the username
                .passwordEncoder(bCryptPasswordEncoder); // the password encoder encrypts/decrypts the password, to avoid any password info leaking
    }

    /* Configure Authorization of Azra resources */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/css/*", "/js/*", "img/*", "/") // these URLS are visible to all the public
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/Azra/*", "/addNewMember") // only authenticated users can view these ones.
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/") // this is the URL for the log in page.
                .and()
                .logout()
                .logoutUrl("/logOut") // this is the URL For logging out the user
                .and()
                .csrf()
                .disable(); // Disable Cross Site Request Forgery, for now.
    }
}
