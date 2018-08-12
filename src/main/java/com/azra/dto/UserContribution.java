package com.azra.dto;


import com.azra.entities.AzraUser;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserContribution implements Serializable{
    private AzraUser azraUser;
    private Date date;

    public UserContribution(AzraUser azraUser){
        this.azraUser = azraUser;
        date = new Date();
    }

    // helper methods
    public String getTime(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(this.date);
    }

    public String getName(){
        return this.azraUser.getName();
    }

    public String getUsername(){
        return this.azraUser.getUsername();
    }

}
