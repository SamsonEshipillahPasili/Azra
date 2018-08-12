package com.azra.utils;

import com.azra.entities.AzraUser;
import com.azra.repositories.UserRepository;
import com.azra.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DefaultAccountsLoader implements CommandLineRunner{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        // create an admin account
        AzraUser admin = new AzraUser();
        admin.setName("Kevin Admin");
        admin.setGender("Male");
        admin.setPassword(this.bCryptPasswordEncoder.encode("password"));
        admin.setRole("ADMIN");
        admin.setUsername("admin");
        admin.setPhoneNumber("07********");
        admin.setProfileImage(imageService.loadDefaultImage());

        // create an user account
        AzraUser azraUser = new AzraUser();
        azraUser.setName("Kevin User");
        azraUser.setGender("Male");
        azraUser.setPassword(this.bCryptPasswordEncoder.encode("password"));
        azraUser.setRole("USER");
        azraUser.setUsername("user");
        azraUser.setPhoneNumber("07********");
        azraUser.setProfileImage(imageService.loadDefaultImage());

        // save them all to the repository
        userRepository.saveAll(Arrays.asList(admin, azraUser));

    }
}
