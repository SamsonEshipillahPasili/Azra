package com.azra.controllers;

import com.azra.entities.AzraUser;
import com.azra.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AzraRestController {
    @Autowired
    private PaymentService paymentService;



   /* @PostMapping("/Azra/newCycle/{amount}")
    public String createNewPaymentCycle(@PathVariable String amount){
        try{
            // delegate request to service class.
            return paymentService.createPaymentNewPaymentCycle(Integer.parseInt(amount));
        }catch(Exception e){
            e.printStackTrace();
            return "Error";
        }

    }*/

    @PostMapping("/Azra/newCycle/{amount}/{days}")
    public String createNewPaymentCycle(@PathVariable int amount, @PathVariable String days){
        // parse the day, to remove the word "Day" or "Days"
        days = days.toLowerCase().replace("s", "")
                .replace("day", "").trim();
        try{
            // delegate the request to the "paymentService.createNewPaymentCycle()" method.
            return paymentService.createNewPaymentCycle(amount, Integer.parseInt(days));
        }catch(Exception e){
            e.printStackTrace();
            return "Error : " + e.getMessage();
        }
    }

    @PostMapping("/Azra/contribute/{amount}")
    public String contribute(Principal principal, @PathVariable String amount){
        if(principal != null){
            try{
                int amt = Integer.parseInt(amount);
                return this.paymentService.contributeAmount(amt, principal.getName());
            }catch(NumberFormatException nfe){
                nfe.printStackTrace();
                return "Invalid amount specified!";
            }
        }else{
            return "Not Logged In!";
        }
    }

    @PostMapping("/Azra/closeCycle/")
    public String closePaymentCycle(){
        try{
            // delegate request to service class.
            return paymentService.closePaymentCycle();
        }catch(Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
}
