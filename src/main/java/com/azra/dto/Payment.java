package com.azra.dto;

import com.azra.entities.AzraUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Payment here encapsulates when a user will receive payment from the group members.
 */

@Data
public class Payment implements Serializable{
    private AzraUser recipient;
    private Date date;
    private UserContributionsWrapper userContributionsWrapper;

    public Payment(AzraUser azraUser, Date date){
        this.recipient = azraUser;
        this.date = date;
        // there are no contributions yet at the time of creation.
        this.userContributionsWrapper = new UserContributionsWrapper();
    }

    // helper methods
    public String getParsedDate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.date);
    }

    // get the name of the user
    public String getName(){
        return this.recipient.getName();
    }

    // get the phone number
    public String getPhoneNumber(){
        return this.recipient.getPhoneNumber();
    }

    // get the style associated with each recipient, to be replaced with client side rendering
    public String getAssocStyle(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        if(df.format(new Date()).equals(df.format(this.date))){
            return "text-center text-danger";
        }else{
            return "text-center text-success";
        }
    }

    // helper method to get the list of user contributions
    public List<UserContribution> getUserContributions(){
        return this.userContributionsWrapper.getUserContributionList();
    }

    // wrapper to hold the list of contributions
    @Data
    public static class UserContributionsWrapper implements Serializable{
        private List<UserContribution> userContributionList = new ArrayList<>();
    }


}
