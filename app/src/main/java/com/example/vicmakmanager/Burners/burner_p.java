package com.example.vicmakmanager.Burners;

public class burner_p {
    String burner_type, stock, sold, commission;
    int image;

    public burner_p(String burner_type, String stock, String sold, String commission, int image) {
        this.burner_type = burner_type;
        this.stock = stock;
        this.sold = sold;
        this.commission = commission;
        this.image = image;
    }

    public String getBurner_type() {
        return burner_type;
    }

    public void setBurner_type(String burner_type) {
        this.burner_type = burner_type;
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
