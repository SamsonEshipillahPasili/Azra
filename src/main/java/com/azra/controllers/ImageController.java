package com.azra.controllers;

import com.azra.entities.AzraUser;
import com.azra.report.PdfGenerator;
import com.azra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
public class ImageController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/img/{username}/")
    public ResponseEntity<byte[]> getUserProfile(@PathVariable String username){
        if(userRepository.existsById(username)){
            AzraUser azraUser = this.userRepository.findById(username).get();
            if(null != azraUser.getProfileImage()){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                HttpStatus status = HttpStatus.OK;
                return new ResponseEntity<>(azraUser.getProfileImage(), headers, status);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    @GetMapping("/Azra/pdf/report")
    public ResponseEntity<byte[]> getUserProfile() throws Exception {
        String url = "http://localhost:8082/report";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(PdfGenerator.generatePDF(url), headers, status);
    }
}
