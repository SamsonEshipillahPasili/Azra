package com.azra.services;

import com.azra.entities.AzraUser;
import com.azra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AzraUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<AzraUser> userOptional = userRepository.findById(s);
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            throw new UsernameNotFoundException("No user with supplied username");
        }
    }
}
