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
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private AzraUserDetailsService userDetailsService;

    /* Configure Authentication of Azra users */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /* Configure Authorization of Azra resources */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/css/*", "/js/*", "img/*", "/")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/Azra/*", "/addNewMember")
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .and()
                .logout()
                .logoutUrl("/logOut")
                .and()
                .csrf()
                .disable();
    }
}
