package com.azra.services;

import com.azra.dto.ContributionExtras;
import com.azra.dto.Payment;
import com.azra.dto.UserContribution;
import com.azra.entities.AzraUser;
import com.azra.entities.PaymentCycle;
import com.azra.repositories.PaymentCycleRepository;
import com.azra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentCycleRepository paymentCycleRepository;


    public String createPaymentNewPaymentCycle(int amountPerUser){
        Calendar calendar = Calendar.getInstance();

        // adds 24 hours to the current time and returns it.
        Function<Void, Void> addDay = (nullValue) -> {
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            return null;
        };

        // creates a new payment
        Function<AzraUser, Payment> createPayment = azraUser -> {
            Payment payment = new Payment(azraUser, calendar.getTime());
            addDay.apply(null);
            return payment;
        };

        // get a list of all the users
        List<AzraUser> allUsers = this.userRepository.findAllUsers();
        // create a new payment cycle
        PaymentCycle paymentCycle = new PaymentCycle(amountPerUser);
        // map all users to payments
        List<Payment> paymentList = allUsers.stream()
                .map(createPayment)
                .collect(Collectors.toList());

        // set the payment list of the payment cycle
        paymentCycle.setPaymentsWrapper(new PaymentCycle.PaymentsWrapper(paymentList));

        // save the payment to the database
        this.paymentCycleRepository.save(paymentCycle);

        // return acknowledgement
        return "Ok";
    }

    // close a payment cycle
    public String closePaymentCycle(){
        PaymentCycle currentPaymentCycle = this.paymentCycleRepository.getCurrentPaymentCycle(true);
        if(null != currentPaymentCycle){
            currentPaymentCycle.setOpen(false);
            this.paymentCycleRepository.save(currentPaymentCycle);
            return "Ok";
        }
        return "Error";

    }

    // contribute an amount to the system
    public String contributeAmount(int amount, String azraUsername){
        try{
            // get the AzraUser first
            AzraUser contributor = this.userRepository.findById(azraUsername).get();
            if(contributor.getUsername().equals(this.todaysRecipient().getUsername())){
                return "You cannot contribute to yourself!";
            }else{
                // the amount given must tally with the amount per person prevously specified in the cycle.
                if(this.paymentCycleRepository.getCurrentPaymentCycle(true).getAmountPerPerson() != amount){
                    return "The amount must be Ksh." + paymentCycleRepository.getCurrentPaymentCycle(true).getAmountPerPerson();
                }else{
                    // did this user make a previous contribution?
                    Optional<UserContribution> ucOptional = this.getTodaysPayment().getUserContributions().stream()
                            .filter(uc -> uc.getUsername().equals(contributor.getUsername()))
                            .findFirst();
                    // the user made a previous payment
                    if(ucOptional.isPresent()){
                        return "You have already made a contribution!";
                    }else{
                        PaymentCycle currentPaymentCycle = this.paymentCycleRepository.getCurrentPaymentCycle(true);
                        List<Payment> payments = currentPaymentCycle.getPaymentList();
                        payments.stream()
                                .filter(payment -> payment.getParsedDate()
                                        .equals(new SimpleDateFormat("dd/MM/yyyy").format(new Date())))
                                        .findFirst()
                                        .get()
                                        .getUserContributions()
                                        .add(new UserContribution(contributor));
                        // save the mutated cycle back to the repository
                        currentPaymentCycle.getPaymentsWrapper().setPaymentList(payments);
                        this.paymentCycleRepository.save(currentPaymentCycle);
                        return "Ok";
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return "Internal Error";
        }
    }

    // get the today's recipient
    public AzraUser todaysRecipient(){
        if(getTodaysPayment() == null){
            return null;
        }
        return getTodaysPayment().getRecipient();
    }

    // get today's contributions
    public List<UserContribution> todaysContribution(){
        if (this.getTodaysPayment() == null)
            return new ArrayList<>();
        return this.getTodaysPayment().getUserContributions();
    }

    // get today's payment
    public Payment getTodaysPayment(){
        PaymentCycle currentPaymentCycle = this.paymentCycleRepository.getCurrentPaymentCycle(true);
        if (currentPaymentCycle == null) return null;
        // get today's date string
        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        return currentPaymentCycle.getPaymentList().stream()
                .filter(payment -> payment.getParsedDate().equals(dateString))
                .findFirst().orElse(null);
    }

    // get contribution extras
    public ContributionExtras getContributionExtras(String username){
        ContributionExtras contributionExtras = new ContributionExtras();

        if(null != this.paymentCycleRepository.getCurrentPaymentCycle(true)){

            if(this.todaysRecipient() == null) return contributionExtras;
            String todaysRecipient = this.todaysRecipient().getName();
            int contributionTurnout = this.getTodaysPayment() == null ? 0 : this.getTodaysPayment().getUserContributions().size();
            int noOfContributors = this.paymentCycleRepository.getCurrentPaymentCycle(true).getPaymentList().size();
            int totalContributions = contributionTurnout * this.paymentCycleRepository.getCurrentPaymentCycle(true).getAmountPerPerson();
            int possibleContributions = noOfContributors * this.paymentCycleRepository.getCurrentPaymentCycle(true).getAmountPerPerson();
            String date = "";
            Optional<Payment> paymentOptional = this.paymentCycleRepository.getCurrentPaymentCycle(true)
                    .getPaymentList().stream()
                    .filter(payment -> payment.getRecipient().getUsername().equals(username))
                    .findFirst();

            if(paymentOptional.isPresent()){
                date = paymentOptional.get().getParsedDate();
            }

            contributionExtras.setContributionTurnOut(contributionTurnout);
            contributionExtras.setTotalPossibleContributions(possibleContributions);
            contributionExtras.setTotalContributions(totalContributions);
            contributionExtras.setNoOfContributors(noOfContributors);
            contributionExtras.setTodaysRecipient(todaysRecipient);
            contributionExtras.setDateOfPayment(date);
        }
        return contributionExtras;

    }



}
