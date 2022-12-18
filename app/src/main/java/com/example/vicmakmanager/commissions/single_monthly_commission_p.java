package com.example.vicmakmanager.commissions;

import java.util.ArrayList;

public class single_monthly_commission_p {
    String month_name;
    ArrayList<single_day_commission_p> months_daily_commissions;

    public single_monthly_commission_p(String month_name, ArrayList<single_day_commission_p> months_daily_commissions) {
        this.month_name = month_name;
        this.months_daily_commissions = months_daily_commissions;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public ArrayList<single_day_commission_p> getMonths_daily_commissions() {
        return months_daily_commissions;
    }

    public void setMonths_daily_commissions(ArrayList<single_day_commission_p> months_daily_commissions) {
        this.months_daily_commissions = months_daily_commissions;
    }
}
