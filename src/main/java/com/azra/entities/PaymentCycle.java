package com.azra.entities;

import com.azra.dto.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class PaymentCycle implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private boolean isOpen = true;

    @Column
    private int amountPerPerson;
    @Column
    @Lob
    private PaymentsWrapper paymentsWrapper;

    public PaymentCycle(int amountPerPerson){
        this.amountPerPerson = amountPerPerson;
    }

    // wrapper for the payment plans
    @Data
    @AllArgsConstructor
    public static class PaymentsWrapper implements  Serializable{
        private List<Payment> paymentList = new ArrayList<>();
    }


   /* public int getAmountPerPerson(){
        return (int) (this.amountPerPerson * 0.16);
    }*/


    // helper method to quickly get the list of payments
    public List<Payment> getPaymentList(){
        return this.getPaymentsWrapper().getPaymentList();
    }

}

