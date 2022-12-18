package com.example.vicmakmanager;

public class gas_sales_p {
    String gas_name;
    int gas_image;

    String noSold6kg,noSold13kg,noSold25kg;
    String credit6kg, credit13kg,credit25kg;
    String cash6kg, cash13kg,cash25kg;

    String noSoldTotal, creditTotal, cashTotal;

    public gas_sales_p(String gas_name, int gas_image, String noSold6kg, String noSold13kg, String noSold25kg, String credit6kg,
                       String credit13kg, String credit25kg, String cash6kg, String cash13kg, String cash25kg, String noSoldTotal,
                       String creditTotal, String cashTotal) {
        this.gas_name = gas_name;
        this.gas_image = gas_image;
        this.noSold6kg = noSold6kg;
        this.noSold13kg = noSold13kg;
        this.noSold25kg = noSold25kg;
        this.credit6kg = credit6kg;
        this.credit13kg = credit13kg;
        this.credit25kg = credit25kg;
        this.cash6kg = cash6kg;
        this.cash13kg = cash13kg;
        this.cash25kg = cash25kg;
        this.noSoldTotal = noSoldTotal;
        this.creditTotal = creditTotal;
        this.cashTotal = cashTotal;
    }

    public String getGas_name() {
        return gas_name;
    }

    public void setGas_name(String gas_name) {
        this.gas_name = gas_name;
    }

    public int getGas_image() {
        return gas_image;
    }

    public void setGas_image(int gas_image) {
        this.gas_image = gas_image;
    }

    public String getNoSold6kg() {
        return noSold6kg;
    }

    public void setNoSold6kg(String noSold6kg) {
        this.noSold6kg = noSold6kg;
    }

    public String getNoSold13kg() {
        return noSold13kg;
    }

    public void setNoSold13kg(String noSold13kg) {
        this.noSold13kg = noSold13kg;
    }

    public String getNoSold25kg() {
        return noSold25kg;
    }

    public void setNoSold25kg(String noSold25kg) {
        this.noSold25kg = noSold25kg;
    }

    public String getCredit6kg() {
        return credit6kg;
    }

    public void setCredit6kg(String credit6kg) {
        this.credit6kg = credit6kg;
    }

    public String getCredit13kg() {
        return credit13kg;
    }

    public void setCredit13kg(String credit13kg) {
        this.credit13kg = credit13kg;
    }

    public String getCredit25kg() {
        return credit25kg;
    }

    public void setCredit25kg(String credit25kg) {
        this.credit25kg = credit25kg;
    }

    public String getCash6kg() {
        return cash6kg;
    }

    public void setCash6kg(String cash6kg) {
        this.cash6kg = cash6kg;
    }

    public String getCash13kg() {
        return cash13kg;
    }

    public void setCash13kg(String cash13kg) {
        this.cash13kg = cash13kg;
    }

    public String getCash25kg() {
        return cash25kg;
    }

    public void setCash25kg(String cash25kg) {
        this.cash25kg = cash25kg;
    }

    public String getNoSoldTotal() {
        return noSoldTotal;
    }

    public void setNoSoldTotal(String noSoldTotal) {
        this.noSoldTotal = noSoldTotal;
    }

    public String getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(String creditTotal) {
        this.creditTotal = creditTotal;
    }

    public String getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(String cashTotal) {
        this.cashTotal = cashTotal;
    }
}
