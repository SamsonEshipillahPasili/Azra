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
}
