package com.example.vicmakmanager;

public class burner_regulator_pipes_grills_sales_p {

    String item_type;
    int burner_image;
    String burner_type, no_sold, no_credit, no_cash;

    public burner_regulator_pipes_grills_sales_p(String item_type, int burner_image, String burner_type, String no_sold, String no_credit, String no_cash) {
        this.item_type = item_type;
        this.burner_image = burner_image;
        this.burner_type = burner_type;
        this.no_sold = no_sold;
        this.no_credit = no_credit;
        this.no_cash = no_cash;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getBurner_image() {
        return burner_image;
    }

    public void setBurner_image(int burner_image) {
        this.burner_image = burner_image;
    }

    public String getBurner_type() {
        return burner_type;
    }

    public void setBurner_type(String burner_type) {
        this.burner_type = burner_type;
    }

    public String getNo_sold() {
        return no_sold;
    }

    public void setNo_sold(String no_sold) {
        this.no_sold = no_sold;
    }

    public String getNo_credit() {
        return no_credit;
    }

    public void setNo_credit(String no_credit) {
        this.no_credit = no_credit;
    }

    public String getNo_cash() {
        return no_cash;
    }

    public void setNo_cash(String no_cash) {
        this.no_cash = no_cash;
    }
}
