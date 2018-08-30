package com.azra.dto;

/* store contributions extras, to be used by the model in a single class */

import lombok.Data;

@Data
public class ContributionExtras {
    private String dateOfPayment;
    private int contributionTurnOut;
    private int totalContributions;
    private int totalPossibleContributions;
    private int noOfContributors;
    private String todaysRecipient = "Not Selected.";

    public int getTotalContributions(){
        return (int) Math.round(this.totalContributions * 0.16);
    }

    public int getTotalPossibleContributions(){
        return (int) Math.round(this.totalPossibleContributions * 0.16);
    }
}
