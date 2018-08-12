package com.azra.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

@Service
public class ImageService {

    public byte[] loadDefaultImage(){
        try {
            URL url = new URL("http://localhost:8082/img/avatar.png");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ArrayList<Byte> byteArrayList = new ArrayList<>();
            int value;
            while((value = is.read()) != -1){
                 byteArrayList.add(Integer.valueOf(value).byteValue());
            }
            byte[] rValue = new byte[byteArrayList.size()];
            for(int i = 0; i < byteArrayList.size(); i++){
                rValue[i] = byteArrayList.get(i);
            }
            return rValue;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
