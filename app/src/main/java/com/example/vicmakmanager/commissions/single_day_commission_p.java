package com.example.vicmakmanager.commissions;

public class single_day_commission_p {
    String date, gas_commission, burner_commission, grill_commission, pipe_commission,regulator_commission;
    String month;

    public single_day_commission_p(String date, String gas_commission, String burner_commission, String grill_commission,
                                   String pipe_commission, String regulator_commission, String month) {
        this.date = date;
        this.gas_commission = gas_commission;
        this.burner_commission = burner_commission;
        this.grill_commission = grill_commission;
        this.pipe_commission = pipe_commission;
        this.regulator_commission = regulator_commission;
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGas_commission() {
        return gas_commission;
    }

    public void setGas_commission(String gas_commission) {
        this.gas_commission = gas_commission;
    }

    public String getBurner_commission() {
        return burner_commission;
    }

    public void setBurner_commission(String burner_commission) {
        this.burner_commission = burner_commission;
    }

    public String getGrill_commission() {
        return grill_commission;
    }

    public void setGrill_commission(String grill_commission) {
        this.grill_commission = grill_commission;
    }

    public String getPipe_commission() {
        return pipe_commission;
    }

    public void setPipe_commission(String pipe_commission) {
        this.pipe_commission = pipe_commission;
    }

    public String getRegulator_commission() {
        return regulator_commission;
    }

    public void setRegulator_commission(String regulator_commission) {
        this.regulator_commission = regulator_commission;
    }
}
