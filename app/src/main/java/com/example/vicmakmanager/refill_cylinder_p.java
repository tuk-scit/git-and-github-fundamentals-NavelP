package com.example.vicmakmanager;

public class refill_cylinder_p {
    String image_url, gas_name, gas_weight, gas_price;

    public refill_cylinder_p(String image_url, String gas_name, String gas_weight, String gas_price) {
        this.image_url = image_url;
        this.gas_name = gas_name;
        this.gas_weight = gas_weight;
        this.gas_price = gas_price;
    }

    public String getGas_price() {
        return gas_price;
    }

    public void setGas_price(String gas_price) {
        this.gas_price = gas_price;
    }

    public String getGas_name() {
        return gas_name;
    }

    public void setGas_name(String gas_name) {
        this.gas_name = gas_name;
    }

    public String getGas_weight() {
        return gas_weight;
    }

    public void setGas_weight(String gas_weight) {
        this.gas_weight = gas_weight;
    }

    public refill_cylinder_p(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
