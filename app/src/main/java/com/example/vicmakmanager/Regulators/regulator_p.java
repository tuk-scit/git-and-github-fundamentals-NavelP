package com.example.vicmakmanager.Regulators;

public class regulator_p {
    String regulator_type, stock, sold, commission;
    int image;

    public regulator_p(String regulator_type, String stock, String sold, String commission, int image) {
        this.regulator_type = regulator_type;
        this.stock = stock;
        this.sold = sold;
        this.commission = commission;
        this.image = image;
    }

    public String getRegulator_type() {
        return regulator_type;
    }

    public void setRegulator_type(String regulator_type) {
        this.regulator_type = regulator_type;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
