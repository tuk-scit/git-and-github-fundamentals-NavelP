package com.example.vicmakmanager;

public class credits_p {
    String phoneNumber, credit_amount, item_name, vicmakShop;

    public credits_p(String phoneNumber, String credit_amount, String item_name, String vicmakShop) {
        this.phoneNumber = phoneNumber;
        this.credit_amount = credit_amount;
        this.item_name = item_name;
        this.vicmakShop = vicmakShop;
    }

    public String getVicmakShop() {
        return vicmakShop;
    }

    public void setVicmakShop(String vicmakShop) {
        this.vicmakShop = vicmakShop;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(String credit_amount) {
        this.credit_amount = credit_amount;
    }
}
