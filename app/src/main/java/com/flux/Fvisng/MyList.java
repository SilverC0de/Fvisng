package com.flux.Fvisng;

public class MyList {
    private String amount, interest, final_amount, ref, purpose, status, duration, approved_date, repayment_date;


    public MyList(String amount, String interest, String final_amount, String ref, String purpose, String status, String duration, String approved_date, String repayment_date) {
        this.amount = amount;
        this.interest = interest;
        this.final_amount = final_amount;
        this.ref = ref;
        this.purpose = purpose;
        this.status = status;
        this.duration = duration;
        this.approved_date = approved_date;
        this.repayment_date = repayment_date;
    }

    public String getAmount() {
        return amount;
    }

    public String getInterest() {
        return interest;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public String getRef() {
        return ref;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    public String getDuration() {
        return duration;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public String getRepayment_date() {
        return repayment_date;
    }
}
